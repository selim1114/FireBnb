package com.example.firebnb.domaine.services

import android.util.Log
import com.example.firebnb.domaine.entité.Reservation
import com.example.firebnb.sourceDeDonnees.SourceDeDonnées

class RéservationService(private val source: SourceDeDonnées) {

    suspend fun obtenirRéservationsEnCours(): List<Reservation> {
        return source.obtenirRéservations()
            .filter { it.status == Reservation.Status.EN_ATTENTE || it.status == Reservation.Status.ACCEPTÉ }
    }

    suspend fun obtenirHistoriqueRéservations(): List<Reservation> {
        return source.obtenirRéservations()
            .filter { it.status == Reservation.Status.COMPLÉTÉ || it.status == Reservation.Status.ANNULÉ || it.status == Reservation.Status.REFUSÉ }
    }

    suspend fun ajouterRéservation(reservation: Reservation) {
        Log.d("ReservationService", "Ajout de la réservation: $reservation")
        source.ajouterRéservation(reservation)
    }

    suspend fun supprimerRéservation(reservation: Reservation) {
        source.supprimerRéservation(reservation)
    }

    suspend fun obtenirReservationParId(id: String): Reservation? {
        return source.obtenirRéservationParId(id)
    }
}
