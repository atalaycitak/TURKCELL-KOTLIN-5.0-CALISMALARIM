package oop_odev

// Kalıtım (Inheritance), bir sınıfın (çocuk), başka bir sınıfın (ebeveyn) tüm özelliklerini 
// ve yeteneklerini miras almasıdır. Bu, kod tekrarını önler. 
// "Bütün arabaların dört tekerleği ve rengi vardır, hepsini baştan her arabaya yazmaya gerek yok" mantığıdır.

// Kotlin'de sınıflar varsayılan olarak kilitlidir (final). Miras alınabilmeleri için 'open' diyoruz.
open class StandartAraba(val uretimYili: Int, var renk: String) {
    
    // Alt sınıfların isterse değiştirebilmesi için metoda da 'open' diyoruz.
    open fun kornayaBas() {
        println("Bip Bip! (Standart Korna)")
    }

    // Bu metod miras verilen bütün sınıflarda direkt aynı şekilde kullanılacak
    fun boyat(yeniRenk: String) {
        renk = yeniRenk
        println("Arabanın rengi '$yeniRenk' olarak değiştirildi.")
    }
}

// SporAraba sınıfı, StandartAraba'dan miras alıyor. ( : StandartAraba(...) şeklinde )
// Kendisine ait esktra 'maksimumHiz' özelliği var.
class SporAraba(uretimYili: Int, renk: String, val maksimumHiz: Int) : StandartAraba(uretimYili, renk) {
    
    // Miras aldığımız kornayaBas metodunu 'override' ederek (ezerek) kendimize göre uyarlıyoruz.
    override fun kornayaBas() {
        println(" Çok yüksek sesli ve spor korna")
    }

    // Sadece spor arabaya özel, standart arabada olmayan yeni bir yetenek
    fun turboAc() {
        println("Turbo aktif edildi! Araç hızla $maksimumHiz km/sa hıza ulaşıyor!")
    }
}
