
# Ticket API — Mobil Geliştirici Dokümanı

Bu doküman, Kotlin mobil uygulamanın tüketeceği REST API'yi anlatır. API Express.js + PostgreSQL üzerinde çalışır.

---

## 1. Temel Bilgiler

| Konu | Değer |
| --- | --- |
| Base URL (lokal) | `http://localhost:3000` |
| Format | JSON (`Content-Type: application/json`) |
| Auth şeması | `Authorization: Bearer <accessToken>` |
| Para birimi | `*Cents` alanları **kuruş** (integer). Örn. `priceCents: 20000` = ₺200,00 |
| Tarih formatı | ISO-8601 UTC (`2026-05-19T18:00:00.000Z`) |
| ID formatı | UUID v4 |
| Karakter seti | UTF-8 |
| Swagger UI | `GET /docs` |
| OpenAPI JSON | `GET /docs.json` |

### Seed kullanıcıları (geliştirme)
| Email | Şifre | Rol |
| --- | --- | --- |
| `admin@example.com` | `admin123` | ADMIN |
| `staff@example.com` | `staff123` | STAFF |
| `user@example.com` | `user123` | USER |

---

## 2. Roller ve Erişim

- **USER** — Bilet alan son kullanıcı. Etkinlikleri görür, sat alım yapar, biletini görüntüler.
- **STAFF** — Kapı görevlisi. Yalnızca kendisine atanmış etkinliklerin biletlerini tarayabilir.
- **ADMIN** — Etkinlik/bilet türü yönetimi, staff atama, satış raporu. STAFF yetkilerini de kapsar.

Her endpoint'in altında "Yetki" satırı vardır:
- *Public* — Token gerekmez.
- *Authenticated* — Geçerli access token gerekir.
- *USER / STAFF / ADMIN* — Listelenen rollerden biri olmalıdır.

---

## 3. Token Akışı

1. `POST /auth/login` → `{ accessToken, refreshToken, user }`
2. Access token 15 dakika geçerli, refresh token 7 gün geçerli.
3. `accessToken` her korumalı istekte `Authorization: Bearer ...` başlığında gönderilir.
4. 401 alırsan `POST /auth/refresh` ile yeni token çifti al; eski refresh otomatik iptal olur (rotation).
5. Cihaz "logout" deyince `POST /auth/logout` ile refresh token'ı sunucudan revoke et.

> Mobil tarafta access ve refresh token'ı **şifreli** saklayın (Android Keystore / EncryptedSharedPreferences).

---

## 4. Hata Formatı

Tüm hatalar şu zarfta döner:

```json
{
  "error": {
    "code": "validation_error",
    "message": "İstek doğrulaması başarısız",
    "details": { /* opsiyonel: zod field hataları */ }
  }
}
```

**Sık karşılaşılan kodlar**

| HTTP | code | Anlam |
| --- | --- | --- |
| 400 | `validation_error` | İstek gövdesi/parametresi geçersiz |
| 400 | `invalid_state` | Kaynak bu işlem için uygun durumda değil |
| 401 | `missing_token` / `invalid_token` | Token yok / geçersiz |
| 401 | `invalid_credentials` | Email veya şifre hatalı |
| 401 | `invalid_refresh` | Refresh token geçersiz/süresi dolmuş |
| 403 | `forbidden` | Rol yetersiz |
| 403 | `not_assigned` | Staff bu etkinliğe atanmamış |
| 403 | `not_purchase_owner` | Sat alım sahibi değilsin |
| 404 | `event_not_found` / `ticket_type_not_found` / `purchase_not_found` / `ticket_not_found` / `route_not_found` | Kayıt yok |
| 409 | `email_taken` | Email zaten kayıtlı |
| 409 | `capacity_exceeded` | Stok yetersiz |
| 409 | `already_paid` | Sat alım zaten ödenmiş |
| 409 | `already_used` | Bilet daha önce kullanılmış |
| 500 | `internal_error` | Sunucu hatası |

---

## 5. Endpoint Referansı

### 5.1 Auth

#### `POST /auth/register`
Yeni kullanıcı kaydı. Otomatik **USER** rolüyle açılır ve token çifti döner — kullanıcıyı ayrıca login etmenize gerek yok.

- **Yetki:** Public
- **Request**
  ```json
  { "email": "ali@example.com", "password": "guclu-sifre-12" }
  ```
    - `email` — geçerli email, otomatik lowercase'e çevrilir
    - `password` — 8–128 karakter
- **201 Response**
  ```json
  {
    "user": { "id": "uuid", "email": "ali@example.com", "role": "USER" },
    "accessToken": "eyJ...",
    "refreshToken": "eyJ..."
  }
  ```
- **Hatalar:** 400 `validation_error`, 409 `email_taken`

---

#### `POST /auth/login`
Mevcut kullanıcı girişi.

- **Yetki:** Public
- **Request**
  ```json
  { "email": "user@example.com", "password": "user123" }
  ```
- **200 Response:** Register ile aynı yapı.
- **Hatalar:** 400, 401 `invalid_credentials`

---

#### `POST /auth/refresh`
Eski refresh ile yeni access + refresh çifti üretir. Eski refresh anında geçersizleşir (token rotation).

- **Yetki:** Public (refresh token gövdede gönderilir)
- **Request**
  ```json
  { "refreshToken": "eyJ..." }
  ```
- **200 Response**
  ```json
  { "accessToken": "eyJ...", "refreshToken": "eyJ..." }
  ```
- **Hatalar:** 401 `invalid_refresh`

---

#### `POST /auth/logout`
Refresh token'ı sunucudan iptal eder. Cihazdaki tokenları da silmeyi unutmayın.

- **Yetki:** Public (token gövdede)
- **Request:** `{ "refreshToken": "..." }`
- **204 Response:** Boş

---

### 5.2 Etkinlikler (genel)

#### `GET /events`
Etkinlikleri listeler.

- **Yetki:** Authenticated
- **Query parametreleri**
    - `upcoming` (opsiyonel) — `true` veya `false`. Yalnızca gelecekteki etkinlikleri filtreler.
- **200 Response**
  ```json
  [
    {
      "id": "uuid",
      "name": "Konser",
      "description": "Açıklama",
      "place": "İstanbul",
      "startsAt": "2026-05-19T18:00:00.000Z",
      "endsAt": "2026-05-19T22:00:00.000Z",
      "ticketTypes": [
        {
          "id": "uuid",
          "name": "VIP",
          "priceCents": 20000,
          "capacity": 100,
          "soldCount": 5,
          "remaining": 95
        }
      ]
    }
  ]
  ```

---

#### `GET /events/{id}`
Tekil etkinlik detayı.

- **Yetki:** Authenticated
- **Path parametresi:** `id` — UUID
- **200 Response:** Yukarıdaki Event nesnesiyle aynı yapı.
- **Hatalar:** 404 `event_not_found`

---

### 5.3 Satın Alma

#### `POST /purchases`
Yeni satın alım oluşturur. Durum **PENDING** olarak döner.

- **Yetki:** USER
- **Request**
  ```json
  {
    "items": [
      { "ticketTypeId": "uuid", "quantity": 2 }
    ]
  }
  ```
    - `ticketTypeId` — UUID, geçerli bir bilet türü
    - `quantity` — 1–20 arası integer
- **201 Response**
  ```json
  {
    "id": "uuid",
    "status": "PENDING",
    "totalCents": 40000,
    "paidAt": null,
    "items": [
      {
        "id": "uuid",
        "ticketTypeId": "uuid",
        "quantity": 2,
        "unitPriceCents": 20000
      }
    ],
    "tickets": []
  }
  ```
- **Hatalar:** 400 `validation_error`, 409 `capacity_exceeded`

---

#### `POST /purchases/{id}/pay`
Mock ödeme işlemi. Başarılıysa durum **PAID** olur ve biletler üretilir.

- **Yetki:** USER (satın alım sahibi)
- **Path parametresi:** `id` — UUID
- **Request:** Boş gövde
- **200 Response**
  ```json
  {
    "id": "uuid",
    "status": "PAID",
    "totalCents": 40000,
    "paidAt": "2026-05-19T18:05:00.000Z",
    "items": [
      {
        "id": "uuid",
        "ticketTypeId": "uuid",
        "quantity": 2,
        "unitPriceCents": 20000
      }
    ],
    "tickets": [
      {
        "id": "uuid",
        "qrCode": "uuid",
        "status": "VALID",
        "ticketTypeId": "uuid"
      }
    ]
  }
  ```
- **Hatalar:** 403 `not_purchase_owner`, 404 `purchase_not_found`, 409 `already_paid`

---

#### `GET /purchases/{id}`
Tekil satın alım detayı.

- **Yetki:** USER (satın alım sahibi)
- **Path parametresi:** `id` — UUID
- **200 Response:** Yukarıdaki Purchase yapısıyla aynı.
- **Hatalar:** 403 `not_purchase_owner`, 404 `purchase_not_found`

---

### 5.4 Kullanıcı (Me)

#### `GET /me/purchases`
Giriş yapmış kullanıcının tüm satın alımlarını listeler.

- **Yetki:** Authenticated
- **200 Response:** `Purchase[]` — Yukarıdaki yapıda dizi.

---

#### `GET /me/tickets`
Giriş yapmış kullanıcının tüm biletlerini listeler.

- **Yetki:** Authenticated
- **200 Response**
  ```json
  [
    {
      "id": "uuid",
      "qrCode": "uuid",
      "status": "VALID",
      "usedAt": null,
      "ticketType": {
        "id": "uuid",
        "name": "VIP",
        "priceCents": 20000,
        "event": {
          "id": "uuid",
          "name": "Konser",
          "place": "İstanbul",
          "startsAt": "2026-05-19T18:00:00.000Z"
        }
      }
    }
  ]
  ```

---

#### `GET /me/tickets/{id}`
Tekil bilet detayı.

- **Yetki:** Authenticated
- **Path parametresi:** `id` — UUID
- **200 Response:** Yukarıdaki Ticket yapısıyla aynı.
- **Hatalar:** 404 `ticket_not_found`

---

### 5.5 Admin

#### `POST /admin/events`
Yeni etkinlik oluşturur.

- **Yetki:** ADMIN
- **Request**
  ```json
  {
    "name": "Konser",
    "description": "Açıklama",
    "place": "İstanbul",
    "startsAt": "2026-05-19T18:00:00.000Z",
    "endsAt": "2026-05-19T22:00:00.000Z"
  }
  ```
- **201 Response:** Event nesnesi.
- **Hatalar:** 400 `validation_error`, 403 `forbidden`

---

#### `PATCH /admin/events/{id}`
Etkinlik günceller.

- **Yetki:** ADMIN
- **Path parametresi:** `id` — UUID
- **Request:** EventCreate ile aynı yapı (kısmi güncelleme).
- **200 Response:** Güncellenmiş Event nesnesi.
- **Hatalar:** 403 `forbidden`, 404 `event_not_found`

---

#### `DELETE /admin/events/{id}`
Etkinlik siler.

- **Yetki:** ADMIN
- **Path parametresi:** `id` — UUID
- **204 Response:** Boş
- **Hatalar:** 403 `forbidden`, 404 `event_not_found`

---

#### `GET /admin/events/{id}/sales`
Etkinliğin satış özetini döner.

- **Yetki:** ADMIN
- **Path parametresi:** `id` — UUID
- **200 Response:** Satış özet nesnesi.
- **Hatalar:** 403 `forbidden`, 404 `event_not_found`

---

#### `POST /admin/events/{id}/ticket-types`
Etkinliğe yeni bilet türü ekler.

- **Yetki:** ADMIN
- **Path parametresi:** `id` — UUID (etkinlik)
- **Request**
  ```json
  {
    "name": "VIP",
    "priceCents": 20000,
    "capacity": 100
  }
  ```
    - `priceCents` — 0 veya üzeri integer
    - `capacity` — 1 veya üzeri integer
- **201 Response:** TicketType nesnesi.
- **Hatalar:** 400 `validation_error`, 403 `forbidden`, 404 `event_not_found`

---

#### `PATCH /admin/ticket-types/{id}`
Bilet türünü günceller.

- **Yetki:** ADMIN
- **Path parametresi:** `id` — UUID (bilet türü)
- **Request:** TicketTypeCreate ile aynı yapı.
- **200 Response:** Güncellenmiş TicketType nesnesi.
- **Hatalar:** 403 `forbidden`, 404 `ticket_type_not_found`

---

#### `POST /admin/events/{eventId}/staff/{userId}`
Etkinliğe staff atar.

- **Yetki:** ADMIN
- **Path parametreleri:** `eventId`, `userId` — UUID
- **201 Response:** Boş
- **Hatalar:** 403 `forbidden`, 404 `event_not_found`

---

#### `DELETE /admin/events/{eventId}/staff/{userId}`
Etkinlikten staff atamasını kaldırır.

- **Yetki:** ADMIN
- **Path parametreleri:** `eventId`, `userId` — UUID
- **204 Response:** Boş
- **Hatalar:** 403 `forbidden`

---

### 5.6 Check-in (Staff)

#### `GET /checkin/events`
Staff'ın atandığı etkinlikleri listeler.

- **Yetki:** STAFF / ADMIN
- **200 Response:** `Event[]`

---

#### `POST /checkin/scan`
QR kod tarayarak bileti kullanılmış olarak işaretler.

- **Yetki:** STAFF / ADMIN (etkinliğe atanmış olmalı)
- **Request**
  ```json
  { "qrCode": "uuid" }
  ```
- **200 Response**
  ```json
  {
    "ticketId": "uuid",
    "ticketType": "VIP",
    "event": { "...Event nesnesi..." },
    "checkedInAt": "2026-05-19T18:30:00.000Z"
  }
  ```
- **Hatalar:** 403 `not_assigned`, 404 `ticket_not_found`, 409 `already_used`
