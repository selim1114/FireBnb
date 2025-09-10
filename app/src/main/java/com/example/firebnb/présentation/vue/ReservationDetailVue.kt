package com.example.firebnb.présentation.vue

import com.example.firebnb.domaine.entité.Reservation

interface ReservationDetailVue {
    fun afficherDetails(reservation: Reservation)
    fun afficherErreur(message: String)
    fun afficherMessage(message: String)
}