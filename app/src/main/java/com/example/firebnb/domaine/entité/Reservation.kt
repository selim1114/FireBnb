package com.example.firebnb.domaine.entité

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
data class Reservation(
    var id: Int,
    var proprieteId: Int,
    var dateArrivee: LocalDate,
    var dateSortie: LocalDate,
    var dateReservation: LocalDate,
    var prix: Double,
    var status: Status,
    var titre: String,
    var imageUrl: String,
    var ville: String = "",
    var pays: String = ""
) {
    enum class Status { EN_ATTENTE, ACCEPTÉ, REFUSÉ, COMPLÉTÉ, ANNULÉ }

    init {
        require(prix >= 0.0) { "Le prix de la propriété ne peut pas être négatif" }
        require(dateArrivee.isBefore(dateSortie)) { "vous pouvez pas choisir une date de sortie avant la date d'arrivée" }

    }
}