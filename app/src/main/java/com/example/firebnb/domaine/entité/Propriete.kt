package com.example.firebnb.domaine.entité

import android.icu.util.DateInterval

class Propriete(
    var id : Int,
    var titre: String,
    var description: String,
    var adresse: String,
    var longitude: Double,
    var latitude: Double,
    var nbChambre: Int,
    var nbSallesDeBain: Int,
    var capacitéMax: Int,
    var prix: Double,
    var photo: String,
    var equipements: ArrayList<String>,
    var dateDisponible: List<DateInterval>,
    var animauxAcceptes: Boolean,
) {
init{
    require(prix >=0.0){"Le prix de la propriété ne peut pas être négatif"}
    require(nbChambre >=0){"Le nombre de chambres ne peut pas être négatif"}
    require(titre.isNotEmpty()){"Le titre ne peut pas être vide"}
    require(adresse.isNotEmpty()){"L'adresse ne peut pas être vide"}
    require(latitude in -90.0..90.0) { "La latitude doit être comprise entre -90 et 90" }
    require(longitude in -180.0..180.0) { "La longitude doit être comprise entre -180 et 180" }
    }
}
