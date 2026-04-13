package oop_odev

// Çok Biçimlilik (Polymorphism), aynı metodun farklı sınıflarda çağrıldığında 
// farklı şekillerde (kendi yapılarına özgü) davranma yeteneğidir. 
// "Her taşıta 'hareket et' dediğimizde, hepsi hareket eder ama BİÇİMLERİ farklıdır."

open class Tasit {
    open fun calis() {
        println("Taşıt çalışmaya başlıyor...")
    }
}

class BenzinliMotorluArac : Tasit() {
    override fun calis() {
        println("Benzinli Araç: Marşa basıldı, benzin pompalanıyor, motor çalıştı!")
    }
}

class ElektrikliMotorluArac : Tasit() {
    override fun calis() {
        println("Elektrikli Araç: Sadece bir düğmeye basıldı, ışıklar yandı, tamamen sessiz çalıştı.")
    }
}

class UcanAraba : Tasit() {
    override fun calis() {
        println("Uçan Araba: Kanatlar açıldı, jet motorları ateşlendi")
    }
}

// Çok biçimlilik burada parlıyor:
// Aşağıdaki fonksiyonda parametre olarak üst sınıf olan 'Tasit' bekliyoruz.
// Sürpriz burada yatar: Buraya HANGİ aracı verirsek verelim 'calis()' dediğimizde 
// içerideki 'if' lere gerek kalmadan her nesne kendi 'calis()' davranışını bilir ve onu yapar.
fun garajdakiAraciCalistir(arac: Tasit) {
    arac.calis() 
}

/*
Kullanım Örneği (Hayali Main Metodu):
fun main() {
    val tesla = ElektrikliMotorluArac()
    val mustang = BenzinliMotorluArac()
    val modelX_Sky = UcanAraba()

    garajdakiAraciCalistir(tesla)    // Sessiz çalışma metodu çağrılır
    garajdakiAraciCalistir(mustang)  // DRR DRR motor sesi metodu çağrılır
    garajdakiAraciCalistir(modelX_Sky)// Jet motoru metodu çağrılır
}
*/
