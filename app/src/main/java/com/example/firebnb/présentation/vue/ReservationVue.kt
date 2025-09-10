package com.example.firebnb.présentation.vue

import android.os.Bundle
import com.example.firebnb.domaine.entité.Propriete
import com.example.firebnb.domaine.entité.Reservation

interface ReservationVue {
    fun afficherRéservations(reservations: List<Reservation>)
    fun notifierItemInséré(position: Int)
    fun notifierItemSupprimé(position: Int)
    fun afficherErreur(message: String)
    fun naviguerVersDétails(bundle: Bundle)

}