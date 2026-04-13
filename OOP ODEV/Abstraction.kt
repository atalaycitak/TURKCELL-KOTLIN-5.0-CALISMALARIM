package oop_odev

// Soyutlama (Abstraction), karmaşık sistemlerin sadece gerekli olan temel özelliklerini 
// öne çıkarıp, gereksiz ve karmaşık arka plan detaylarını gizlemektir.
// Arabayı kullanırken motorun içinde benzinin nasıl yandığını veya elektrikli motorun 
// manyetik alanını bilmemiz gerekmez. Sadece gaz, fren ve direksiyonu biliriz.

// 'abstract' anahtar kelimesi ile bu sınıftan doğrudan bir nesne üretilemeyeceğini belirtiriz.
// Bu sadece bir "şablon"dur.
abstract class Arac(val marka: String, val model: String) {
    
    // Soyut bir metod. Gövdesi yoktur (süslü parantezleri yoktur).
    // Bu şablonu kullanacak olan (miras alacak) alt sınıflar kendi motor tiplerine göre 
    // bu metodu kendileri yazmak ZORUNDADIR.
    abstract fun motoruCalistir()
    
    // Normal bir metod. Soyut sınıf içinde de normal, iş yapan metodlar bulunabilir.
    fun bilgiVer() {
        println("Bu araç: $marka $model")
    }
}

// Arayüzler (Interfaces) de bir soyutlama yöntemidir. Çoklu kalıtım yapılmak istendiğinde kullanılır.
interface SarjEdilebilir {
    fun sarjPrizineBagla()
}

// Şimdi soyut olan Arac'ı temel alarak somut (gerçek) arabalar üretelim.
class ElektrikliAraba(marka: String, model: String) : Arac(marka, model), SarjEdilebilir {
    
    // Soyut metodu ezmek (override) ve içini doldurmak zorundayız!
    override fun motoruCalistir() {
        println("Elektrikli $marka $model sessizce çalıştırıldı. Sistemler devrede.")
    }

    // Arayüzdeki (Interface) metodu da burada tanımlıyoruz.
    override fun sarjPrizineBagla() {
        println("$marka $model şarj ünitesine bağlandı. Batarya doluyor...")
    }
}
