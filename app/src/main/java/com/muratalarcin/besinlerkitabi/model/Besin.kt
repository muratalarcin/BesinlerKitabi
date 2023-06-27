package com.muratalarcin.besinlerkitabi.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity // bunu koyunca işaretlediğim sınıfım sqlite tablosu olmak için hazırlanıyo
data class Besin(
    @ColumnInfo("isim") // kolon adı ayarladık, json daki isim ve benim tanımladığım farklı olmasa gerek kalmazdı
    @SerializedName("isim")// json dosyasındaki isim ile benim tanımladığım isim farklı olduğundan dolayı kullandık, aynı olsa gerek yok
    val besinIsim: String?,
    @ColumnInfo("kalori")
    @SerializedName("kalori")
    val besiKalori: String?,
    @ColumnInfo("karbonhidrat")
    @SerializedName("karbonhidrat")
    val besinKarbonhidrat: String?,
    @SerializedName("protein")
    @ColumnInfo("protein")
    val besinProtein: String?,
    @SerializedName("yag")
    @ColumnInfo("yag")
    val besinYag: String?,
    @ColumnInfo("kalori")
    @SerializedName("gorsel")
    val besinGorsel: String?
    ) {

    @PrimaryKey(autoGenerate = true)  //column info kolonları koyarken id istiyo
    var uuid : Int = 0

}