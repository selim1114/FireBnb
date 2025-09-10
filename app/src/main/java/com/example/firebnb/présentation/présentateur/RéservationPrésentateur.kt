package com.example.firebnb.présentation.présentateur

import com.example.firebnb.domaine.entité.Reservation
import com.example.firebnb.présentation.modèle.Modèle

interface RéservationPrésentateur {
    fun chargerHistoriqueRéservations()
    fun chargerRéservationsEnCours()
    fun gérerClicRéservation(position: Int)
    fun ajouterRéservation(reservation: Reservation)
    fun supprimerRéservation(position: Int)
    fun obtenirNombreRéservations(): Int
    fun obtenirRéservation(position: Int): Reservation
}