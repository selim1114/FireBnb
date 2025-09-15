package com.example.firebnb.présentation.présentateur

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import com.example.firebnb.domaine.entité.Propriete
import com.example.firebnb.présentation.modèle.ModèleImpl
import com.example.firebnb.présentation.vue.DetailVue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.icu.util.DateInterval
import android.os.Build
import android.provider.CalendarContract
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.firebnb.R
import com.example.firebnb.domaine.entité.Reservation
import com.example.firebnb.présentation.ProprieteDTO
import com.example.firebnb.sourceDeDonnees.DatabaseHelper
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Locale
import kotlin.random.Random

class DetailsPropriétéVuePresentateur (
    private val view: DetailVue,
    private val context: Context
) {
    var modèle = ModèleImpl.instance
    private val databaseHelper: DatabaseHelper = DatabaseHelper(context)
    fun rechercherPropriétéSélectionnée() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val proprieteRetournée = withContext(Dispatchers.IO) {
                    modèle.retournerDetailPropriété()
                }
                proprieteRetournée?.let { nonNullPropriete ->
                    val proprieteDTO = mapProprieteADTO(nonNullPropriete)
                    view.afficherDetails(proprieteDTO)
                } ?: run {
                    view.afficherErreur("Propriété non trouvée")
                }
            } catch (e: Exception) {
                view.afficherErreur("Erreur lors de la récupération de la propriété: ${e.message}")
                Log.d("message d'erreure", "${e.message} ")
            }
        }
    }
    fun selectDates(){
        view.selectDates()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onReserveNowClicked(proprieteDTO: ProprieteDTO, dateArrivee: Long, dateDepart: Long) {
        val propriete = mapDTOAPropriete(proprieteDTO)

        val adresseComplete = propriete.adresse
        val (ville, pays) = if (adresseComplete.contains(",")) {
            val index = adresseComplete.lastIndexOf(",")
            val villePart = adresseComplete.substring(0, index).trim()
            val paysPart = adresseComplete.substring(index + 1).trim()
            villePart to paysPart
        } else {
            adresseComplete to ""
        }

        val dateArriveeLocal = Instant.ofEpochMilli(dateArrivee).atZone(ZoneId.systemDefault()).toLocalDate()
        val dateDepartLocal = Instant.ofEpochMilli(dateDepart).atZone(ZoneId.systemDefault()).toLocalDate()

        val nombreJours = ChronoUnit.DAYS.between(dateArriveeLocal, dateDepartLocal).toInt()
        val prixTotal = nombreJours * propriete.prix

        val reservation = Reservation(
            id = Random.nextInt(1000, 9999),
            proprieteId = propriete.id,
            dateArrivee = dateArriveeLocal,
            dateSortie = dateDepartLocal,
            dateReservation = LocalDate.now(),
            prix = prixTotal,
            status = Reservation.Status.EN_ATTENTE,
            titre = propriete.titre,
            imageUrl = propriete.photo,
            ville = ville,
            pays = pays
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                modèle.ajouterRéservation(reservation)
                withContext(Dispatchers.Main) {
                    view.afficherMessage("Réservation ajoutée avec succès.")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    view.afficherErreur("Erreur lors de l'ajout de la réservation : ${e.message}")
                }
            }
        }
    }
    fun getlisteDateContrainte(stringDateConstraint : String){
        view.listDateContrainte = view.convertDTODispoToDateArray(stringDateConstraint)
    }
    fun afficherDateSélectionné(dateArrivéeFormaté: String, dateDépartFormaté: String){
        view.dateChoisie.text =
            view.context?.getString(R.string.DateReserve, dateArrivéeFormaté, dateDépartFormaté)
    }
    fun ajouterAuCalendrier(dateInterval: DateInterval) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Ajouter un évènement")
        builder.setMessage("Voulez-vous ajouter cette réservation à votre calendrier?")
        builder.setPositiveButton("Oui") { dialog, _ ->
            try {
                ouvrirCalendrier(dateInterval)
            } catch (e: Exception) {
                Toast.makeText(context, "Erreur lors de l'ajout au calendrier", Toast.LENGTH_LONG).show()
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Non") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }

    fun ouvrirCalendrier(dateInterval: DateInterval){
        //pour l'activité du calendrier: https://www.youtube.com/watch?v=NK_-phxyIAM et https://itnext.io/android-calendar-intent-8536232ecb38
        var intent = Intent(Intent.ACTION_INSERT)
        intent.setData(CalendarContract.Events.CONTENT_URI)
        intent.putExtra(CalendarContract.Events.TITLE, "firebnb réservation pour ")
        intent.putExtra(CalendarContract.Events.ALL_DAY, false)
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, dateInterval.fromDate)
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, dateInterval.toDate)
        //intent.putExtra(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)

        context.startActivity(intent)
    }


    private fun formatDateInterval(interval: DateInterval): String {
        val formatter = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault())
        val startDate = java.util.Date(interval.fromDate)
        val endDate = java.util.Date(interval.toDate)
        return "${formatter.format(startDate)} to ${formatter.format(endDate)}"
    }
    private fun mapProprieteADTO(propriete: Propriete): ProprieteDTO {
        return ProprieteDTO(
            id = propriete.id.toString(),
            titre = propriete.titre,
            adresse = propriete.adresse,
            longitude = propriete.longitude.toString(),
            latitude = propriete.latitude.toString(),
            description = propriete.description,
            prix = propriete.prix.toString(),
            nbChambre = propriete.nbChambre.toString(),
            capaciteMax = propriete.capacitéMax.toString(),
            nbSallesDeBain = propriete.nbSallesDeBain.toString(),
            photo = propriete.photo,
            equipements = propriete.equipements.joinToString(","),
            dateDisponible = propriete.dateDisponible.joinToString(",") {
                formatDateInterval(it)
            },
            animauxAcceptes = propriete.animauxAcceptes
        )
    }

    fun ajouterFavoris(propriete: ProprieteDTO) {
        try {
            val proprieteEntity = Propriete(
                id = propriete.id.toInt(),
                titre = propriete.titre,
                adresse = propriete.adresse,
                longitude = propriete.longitude.toDouble(),
                latitude = propriete.latitude.toDouble(),
                description = propriete.description,
                prix = propriete.prix.toDouble(),
                nbChambre = propriete.nbChambre.toInt(),
                capacitéMax = propriete.capaciteMax.toInt(),
                nbSallesDeBain = propriete.nbSallesDeBain.toInt(),
                photo = propriete.photo,
                equipements = propriete.equipements.split(",")
                    .map { it.trim() } as ArrayList<String>,
                dateDisponible = listOf(),
                animauxAcceptes = propriete.animauxAcceptes
            )

            databaseHelper.ajouterFavoris(proprieteEntity)
        } catch (e: Exception) {
            view.afficherErreur("Error: ${e.message}")
        }
    }

    private fun mapDTOAPropriete(dto: ProprieteDTO): Propriete {
        return Propriete(
            id = dto.id.toInt(),
            titre = dto.titre,
            adresse = dto.adresse,
            longitude = dto.longitude.toDouble(),
            latitude = dto.latitude.toDouble(),
            description = dto.description,
            prix = dto.prix.toDouble(),
            nbChambre = dto.nbChambre.toInt(),
            photo = dto.photo,
            equipements = dto.equipements.split(",").map { it.trim() } as ArrayList<String>,
            dateDisponible = dto.dateDisponible.split(",").map { interval ->
                val dates = interval.split(" to ")
                val formatter = java.text.SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                val startMillis = formatter.parse(dates[0].trim())?.time ?: throw IllegalArgumentException("Invalid start date")
                val endMillis = formatter.parse(dates[1].trim())?.time ?: throw IllegalArgumentException("Invalid end date")
                DateInterval(startMillis, endMillis)
            } as ArrayList<DateInterval>,
            nbSallesDeBain = dto.nbSallesDeBain.toInt(),
            animauxAcceptes = dto.animauxAcceptes,
            capacitéMax = dto.capaciteMax.toInt()
        )
    }

    fun estProprieteDansFavoris(id: Int): Boolean {
        val favoris = databaseHelper.obtenirsFavoris()
        val isInFavoris = favoris.any { it.id == id }
        return isInFavoris
    }

    fun supprimerFavoris(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                databaseHelper.supprimerFavoris(id)
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    view.afficherErreur("Erreur lors de la suppression : ${e.message}")
                }
            }
        }
    }

    fun navigationEnArrier() {
        view.navigation()
    }

}
