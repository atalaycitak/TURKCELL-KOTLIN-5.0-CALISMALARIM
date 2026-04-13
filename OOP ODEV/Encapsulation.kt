package oop_odev

// Kapsülleme (Encapsulation), veriyi (değişkenleri) ve bu veriyi işleyen fonksiyonları 
// bir arada tutarak dış dünyadan (diğer sınıflardan vb.) gizlemektir.
// Neden gizleriz? Çünkü değişkenlere doğrudan erişilip saçma sapan değerler verilmesini 
// veya uygulamanın çökmesini istemeyiz. Sadece bizim izin verdiğimiz metodlar aracılığıyla 
// kontrollü değişiklik yapılmasına izin veririz. (Kapsülün içi dışarıdan görünmez)

class ArabaMotoru(val tip: String) {
    
    // 'private' sayesinde 'motorSicakligi' değişkenine sadece bu sınıf içerisinden erişilebilir.
    // Başka bir yerden ArabaMotoru.motorSicakligi yazılamaz!
    private var motorSicakligi: Int = 20

    // 'calisiyorMu' değişkenini dışarıdan herkes okuyabilir (get) 
    // ama dışarıdan KİMSE değiştiremez (private set). Böylece dışarıdan hileyle araba çalıştıramazlar.
    var calisiyorMu: Boolean = false
        private set

    // Motoru çalıştırmak için bir metod. Değişkenleri içeride, kontrollü olarak değiştiriyoruz.
    fun calistir() {
        if (!calisiyorMu) {
            calisiyorMu = true
            motorSicakligi += 40
            println("$tip tipli motor çalıştırıldı. Sıcaklık: $motorSicakligi")
        } else {
            println("Hata: Motor zaten çalışıyor.")
        }
    }

    // Gaz vermek için bir metod
    fun gazVer() {
        if (calisiyorMu) {
            motorSicakligi += 15
            println("Gaza basıldı! Motor sıcaklığı arttı: $motorSicakligi")
            
            if (motorSicakligi > 100) {
                println("UYARI: Motor çok ısındı, su kaynatıyor!")
            }
        } else {
            println("Hata: Önce motoru çalıştırmalısınız! Çalışmayan arabada gaz verilmez.")
        }
    }

    // Sıcaklığı doğrudan dışarı açmak yerine güvenli bir yapı sunuyoruz (Getter fonksiyon mantığı)
    fun anlikSicakligiGoster(): Int {
        return motorSicakligi
    }
}
