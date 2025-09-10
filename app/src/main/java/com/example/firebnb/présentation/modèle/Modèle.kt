package com.example.firebnb.présentation.modèle

import com.example.firebnb.domaine.entité.Propriete
import com.example.firebnb.domaine.entité.Reservation
import com.example.firebnb.domaine.entité.FiltreClass

interface Modèle {
    suspend fun obtenirRéservationsEnCours(): List<Reservation>
    suspend fun obtenirHistoriqueRéservations(): List<Reservation>
    suspend fun obtenirPropriétés(): List<Propriete>
    suspend fun obtenirReservationParId(id: String): Reservation?
    suspend fun obtenirProprieteParId(id: Int): Propriete?
    suspend fun retournerDetailPropriété(): Propriete?
    suspend fun ajouterRéservation(reservation: Reservation)
    suspend fun supprimerRéservation(reservation: Reservation)
    suspend fun appliquerFiltre(filtre: FiltreClass)
    suspend fun obtenirPropriétésFiltrées(): List<Propriete>
    suspend fun rechercherPropriétésParCritères(filtre: FiltreClass): List<Propriete>
}