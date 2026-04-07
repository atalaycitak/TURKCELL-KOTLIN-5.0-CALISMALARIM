var bakiye: Double = 1000.0

fun main() {
    print("Sifrenizi giriniz: ")
    val sifre: String = readln()
    
    if (sifre == "1234") {
        println("Giris Basarili.")
        
        while (true) {
            println("\nMENU")
            println("1- Bakiye Sorgula")
            println("2- Para Yatir")
            println("3- Para Cek")
            println("4- Para Transferi")
            println("5- Kredi Basvurusu")
            println("0- Cikis")
            print("Seciminiz: ")
            
            val secim = readln().toInt()
            
            if (secim == 0) {
                println("Bizi tercih ettiginiz icin tesekkurler. Cikis yapildi.")
                break
            } else if (secim == 1) {
                bakiyeSorgula()
            } else if (secim == 2) {
                print("Yatirmak istediginiz tutar: ")
                val tutar = readln().toDouble()
                paraYatir(tutar)
            } else if (secim == 3) {
                print("Cekmek istediginiz tutar: ")
                val tutar = readln().toDouble()
                paraCek(tutar)
            } else if (secim == 4) {
                print("Alici IBAN no: ")
                val iban = readln()
                print("Transfer tutari: ")
                val tutar = readln().toDouble()
                paraTransferi(iban, tutar)
            } else if (secim == 5) {
                print("Kredi tutari: ")
                val tutar = readln().toDouble()
                print("Vade (Ay): ")
                val vade = readln().toInt()
                krediBasvurusu(tutar, vade)
            } else {
                println("Gecersiz secim!")
            }
        }
    } else {
        println("Hatali sifre. Sistem kapatildi.")
    }
}

fun bakiyeSorgula() {
    println("Mevcut Bakiyeniz: $bakiye TL")
}

fun paraYatir(miktar: Double) {
    if (miktar > 0) {
        bakiye += miktar
        println("Hesabiniza $miktar TL yatirildi.")
    } else {
        println("Gecersiz tutar.")
    }
}

fun paraCek(miktar: Double) {
    if (miktar > 0 && miktar <= bakiye) {
        bakiye -= miktar
        println("Hesabinizdan $miktar TL cekildi.")
    } else {
        println("Yetersiz bakiye veya gecersiz islem.")
    }
}

fun paraTransferi(aliciIban: String, miktar: Double) {
    if (miktar > 0 && miktar <= bakiye) {
        bakiye -= miktar
        println("$aliciIban no'lu hesaba $miktar TL transfer edildi. (Kalan bakiye: $bakiye TL)")
    } else {
        println("Yetersiz bakiye. Transfer basarisiz.")
    }
}

fun krediBasvurusu(krediMiktari: Double, vadeAy: Int) {
    if (krediMiktari <= bakiye * 10) {
        println("$vadeAy ay vadeli $krediMiktari TL kredi basvurunuz onaylandi!")
    } else {
        println("Risk degerlendirmesi sonucu krediniz reddedildi.")
    }
}
