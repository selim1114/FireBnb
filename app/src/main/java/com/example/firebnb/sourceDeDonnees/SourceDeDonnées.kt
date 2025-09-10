package com.example.firebnb.sourceDeDonnees

import com.example.firebnb.domaine.entité.Propriete
import com.example.firebnb.domaine.entité.Reservation
import java.time.LocalDate

class SourceDeDonnéesException( message: String) : Exception( message ) {}

interface SourceDeDonnées {
    suspend fun obtenirRéservations(): List<Reservation>
    suspend fun ajouterRéservation(reservation: Reservation)
    suspend fun supprimerRéservation(reservation: Reservation)
    suspend fun obtenirPropriétés(): List<Propriete>
    suspend fun trouverPropriétéParId(id: Int): Propriete?
    suspend fun trouverPropriété(propriete: Propriete): Propriete?
    suspend fun obtenirPropriétéSélectionnée(): Propriete?
    suspend fun obtenirRéservationParId(id: String): Reservation?
    suspend fun rechercherPropriétésParCritères(capacitéMax: Int?, prixParNuit: Double?, adresse: String?, dateDebut: LocalDate?, dateFin: LocalDate?): List<Propriete>

}