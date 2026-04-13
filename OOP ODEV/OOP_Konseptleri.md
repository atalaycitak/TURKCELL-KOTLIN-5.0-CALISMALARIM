# Nesne Yönelimli Programlama (OOP) Konseptleri 

 Tıpkı gerçek dünyada olduğu gibi, yazılım dünyasında da her şey nesnelerden (objeler) oluşur.

Ana Konseptler:
1. **Encapsulation (Kapsülleme)**
2. **Inheritance (Kalıtım)**
3. **Polymorphism (Çok Biçimlilik)**
4. **Abstraction (Soyutlama)**

---

## 1. Encapsulation (Kapsülleme)

**Nedir bu?** 
Kapsülleme, sistemin kritik verilerini bir zırh (kapsül) içine alarak dış dünyanın bunlara kontrolsüz bir şekilde erişmesini engellemektir.

**Arabalarla Düşünelim:**
Bir arabayı kullanırken, motorun içine elini sokup silindirlerin yerini değiştiremezsin ya da benzin enjekte edemezsin. Kaput genelde kapalıdır. Peki sen motoru nasıl çalıştırıyorsun? Kontak anahtarını (veya Start-Stop düğmesini) kullanarak!
Kapsülleme bunu yapar: Motor sıcaklığını veya dönüş hızını dışarıdan erişime kapatır, ama kullanabilmen için sana bir düğme (`calistir()` metodu) sunar.

**Kodda Görelim:**
İncelemek için Encapsulation.kt dosyasına bakabilirsiniz.
* [Satır 12]: `motorSicakligi` değişkeni `private` yapılarak dış dünyadan tamamen saklandı. 
* [Satır 20]: Kapsülün içine müdahale edebileceğin tek güvenli yer olan `calistir()` fonksiyonudur. Araba çalışınca, sıcaklık içeriden güvenli bir şekilde güncellenmektedir.

---

## 2. Inheritance (Kalıtım)

**Nedir?**
Kalıtım, anne-babadan çocuğa geçen özelliklerin yazılımdaki karşılığıdır. Eğer birçok sınıfta benzer özellikler varsa (hepsinde aynı olan şeyler), bunları bir üst sınıfta (ebeveyn) yazarız ve diğer sınıflar (çocuklar) bunları hazırdan kullanır (miras alır). 

**Arabalarla Düşünelim:**
Hiçbir araba markası, yeni bir araba çıkarırken tekerleğin veya kornanın nasıl çalışması gerektiğini sıfırdan "icat etmez". Tüm standart arabaların bir yılı, bir rengi ve bir kornası vardır.
Eğer bir "Spor Araba" üretiyorsak, "Standart Araba"nın sahip olduğu korna ve boya özelliğini hazır olarak alır (miras), üstüne bir de "Turbo" ekleriz.

**Kodda Görelim:**
İncelemek için [Inheritance.kt]dosyasına göz atabilirsiniz.
* [Satır 8]: `StandartAraba` sınıfımızı `open` yaparak diğer sınıflara ebeveynlik yapabileceğini ilan ettik.
* [Satır 22] : `SporAraba`, `: StandartAraba(...)` diyerek standart arabanın tüm mirasını devraldı. 
* [Satır 25] : Miras aldığımız standart kornadan memnun kalmadık, `override` kelimesiyle kendi kornamızı (metodu ezip) tanımladık.

---

## 3. Polymorphism (Çok Biçimlilik)

**Nedir bu?**
Aynı komutun, farklı nesneler üzerinde kendi tarzlarıyla (kendi yapılarına uygun bir şekilde) çalışmasıdır.

**Arabalarla Düşünelim:**
Şu komutu verelim: "Aracı Çalıştır!".
* Eğer araç bir _Mustang_ ise komutu aldıktan sonra yüksek sesle gürler ve benzin yakmaya başlar.
* Eğer araç bir _Tesla_ ise komutu aldıktan sonra tamamen sessiz bir şekilde elektrik devrelerini aktifleştirir.
Gördüğünüz gibi aynı işlemi (`calis()`) herkes **kendi biçiminde** gerçekleştirdi.

**Kodda Görelim:**
Bunu incelemek için hemen [Polymorphism.kt] : `garajdakiAraciCalistir(arac: Tasit)` diye bir komutumuz var. Burası arabanın ne tür olduğuyla hiç ilgilenmez. Parametre olarak genel `Tasit` bekler. 
İster `ElektrikliMotorluArac` ver, ister `UcanAraba` ver... Fonksiyon sadece `.calis()` yazar ve her araba kendi bildiği spesifik sesi / titreşimi çıkartarak işi yapar. 

---

## 4. Abstraction (Soyutlama)

**Nedir bu?**
Karşımızdaki şeyin "ne yaptığıyla" ilgilenip, onu "nasıl yaptığına" kafamızı yormamamızdır. Arka plandaki karmaşık detayların soyutlanması ve bize sadece temel/ana özelliklerin sunulmasıdır.

**Arabalarla Düşünelim:**
Hadi senle bir "Araç Şablonu" tasarlayalım. Biliriz ki dünyadaki her özel aracın bir "MotoruÇalıştır" özelliği olmalı. Fakat ben bu şablonun içini dolduramam. Çünkü bu aracın ne tür bir motorla dizayn edileceğini henüz bilmiyorum. 
İşte "Soyutlama" burada devreye girer. Biz bir sözleşme oluştururuz: *" araba yapıyorsan bir motoru olmak ZORUNDA. Motoru nasıl istiyorsan öyle yap ama o kod buraya yazılmalı."*.

**Kodda Görelim:**
Son olarak [Abstraction.kt]: `Arac` sınıfı `abstract` olarak tanımlandı. Yani direkt bu sınıftan gidip `var arabam = Arac()` yapamayız, çünkü bu tam olmayan bir şablon!
* [Satır 15]: `abstract fun motoruCalistir()` gövdesiz (süslü parantezi olmayan) bir komuttur. Bu sınıfı alan herkesin bu metodu doldurması zorunlu kılındı.
* [Satır 27] : Gerçek ve tamamlanmış bir ürün olan `ElektrikliAraba` şablonu aldıktan sonra, `motoruCalistir` metodunu `override` edip elektrikli arabaya uygun bir şekilde komutlarla içini doldurdu!

---

