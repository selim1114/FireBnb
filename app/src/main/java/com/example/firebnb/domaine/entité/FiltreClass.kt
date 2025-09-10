package com.example.firebnb.domaine.entité

class FiltreClass (
    val destination : String? = null,
    val minPrix: Double?=null,
    val maxPrix : Double?=null,
    val nbChambre:Int?=null,
    val dateArrivée:Long?=null,
    val dateDeparture: Long? = null
) {
    override fun toString(): String {
        return "FiltreClass(destination=$destination, minPrix=$minPrix, maxPrix=$maxPrix, nbChambre=$nbChambre, dateArrivée=$dateArrivée, dateDeparture=$dateDeparture)"
    }
}

