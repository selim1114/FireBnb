package com.example.firebnb.présentation.modèle

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.firebnb.Services.PropriétéService
import com.example.firebnb.domaine.services.RéservationService
import com.example.firebnb.domaine.entité.Propriete
import com.example.firebnb.domaine.entité.Reservation
import com.example.firebnb.domaine.entité.FiltreClass
import com.example.firebnb.sourceDeDonnees.SourceBidon
import com.example.firebnb.sourceDeDonnees.SourceDeDonnéesHttp
import java.time.Instant
import java.time.ZoneId

class ModèleImpl private constructor() : Modèle {
    private var propriétéService: PropriétéService
    private var réservationService: RéservationService
    var filtreActuel: FiltreClass? = null

    init {
        val source = SourceBidon()
        propriétéService = PropriétéService(source = source)
        réservationService = RéservationService(source = source)
    }
 //
   // init {
     //   val urls = mapOf(
       //     "url_proprietes" to "http://idefix.dti.crosemont.quebec:9048/api/propriétés",
         //   "url_reservations" to "http://idefix.dti.crosemont.quebec:9048/api/utilisateurs/1/reservations",
           // "url_recherche_proprietes" to "http://idefix.dti.crosemont.quebec:9048/api/propriétés/recherche"
        //)
        //val sourceHTTP = SourceDeDonnéesHttp(urls)
        //propriétéService = PropriétéService(source = sourceHTTP)
        //réservationService = RéservationService(source = sourceHTTP)
    //}
//

    companion object {
        var instance = ModèleImpl()
    }

    override suspend fun obtenirRéservationsEnCours(): List<Reservation> {
        return réservationService.obtenirRéservationsEnCours()
    }

    override suspend fun obtenirHistoriqueRéservations(): List<Reservation> {
        return réservationService.obtenirHistoriqueRéservations()
    }

    override suspend fun obtenirPropriétés(): List<Propriete> {
        return propriétéService.obtenirPropriétés()
    }

    override suspend fun obtenirReservationParId(id: String): Reservation? {
        return réservationService.obtenirReservationParId(id)
    }

    override suspend fun obtenirProprieteParId(id: Int): Propriete? {
        return propriétéService.obtenirProprieteParId(id)
    }

    override suspend fun retournerDetailPropriété(): Propriete? {
        val propriété = propriétéService.obtenirPropriétéSélectionnée()
        Log.d("Modèle", "Propriete model: ${propriété?.titre}")
        return propriété
    }

    override suspend fun ajouterRéservation(reservation: Reservation) {
        réservationService.ajouterRéservation(reservation)
    }

    override suspend fun supprimerRéservation(reservation: Reservation) {
        réservationService.supprimerRéservation(reservation)
    }

    override suspend fun appliquerFiltre(filtre: FiltreClass) {
        filtreActuel = filtre
    }

    override suspend fun obtenirPropriétésFiltrées(): List<Propriete> {
        val listepropriétés = propriétéService.obtenirPropriétés()
        val filtre = filtreActuel

        return if (filtre != null) {
            listepropriétés.filter { propriété ->
                correspondFilter(propriété, filtre)
            }
        } else {
            listepropriétés
        }
    }

    private fun correspondFilter(property: Propriete, filtre: FiltreClass): Boolean {
        val correspondDestination = filtre.destination?.let { property.adresse.contains(it, ignoreCase = true) }

        val correspondPrix = (filtre.minPrix == null || property.prix >= filtre.minPrix) &&
                (filtre.maxPrix == null || property.prix <= filtre.maxPrix)

        val matchesRooms = filtre.nbChambre == null || property.nbChambre >= filtre.nbChambre

        val correspondDate = if (filtre.dateArrivée == null || filtre.dateDeparture == null) {
            true
        } else {
            dateDisponible(property, filtre)
        }

        return if (correspondDestination == true && correspondPrix && matchesRooms && correspondDate) {
            true
        } else {
            false
        }
    }
    private fun dateDisponible(propriété: Propriete,filtre: FiltreClass): Boolean {
        return propriété.dateDisponible.any { interval ->
            val intervalStart = interval.fromDate
            val intervalEnd = interval.toDate
            val filterStart = filtre.dateArrivée!!
            val filterEnd = filtre.dateDeparture!!
            filterStart <= intervalEnd && filterEnd >= intervalStart
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun rechercherPropriétésParCritères(filtre: FiltreClass): List<Propriete> {
        return propriétéService.rechercherParCritères(
            capacitéMax = filtre.nbChambre,
            prixParNuit = filtre.maxPrix,
            adresse = filtre.destination,
            dateDebut = filtre.dateArrivée?.let { Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate() },
            dateFin = filtre.dateDeparture?.let { Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate() }
        )
    }

}