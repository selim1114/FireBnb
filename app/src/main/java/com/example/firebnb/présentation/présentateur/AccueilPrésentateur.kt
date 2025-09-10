package com.example.firebnb.présentation.présentateur

import android.icu.util.DateInterval
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.firebnb.domaine.entité.Propriete
import com.example.firebnb.domaine.entité.FiltreClass
import com.example.firebnb.présentation.ProprieteDTO
import com.example.firebnb.présentation.modèle.ModèleImpl
import com.example.firebnb.présentation.vue.AccueilVue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AccueilPrésentateur(
    private val view: AccueilVue
) {
    private val modèle = ModèleImpl.instance

    @RequiresApi(Build.VERSION_CODES.O)
    fun chargerPropriétésFiltrées() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val filtre = modèle.filtreActuel

                val filteredProperties = if (filtre == null ||
                    (filtre.destination.isNullOrBlank()
                            && filtre.minPrix == null
                            && filtre.maxPrix == null
                            && filtre.nbChambre == null
                            && filtre.dateArrivée == null
                            && filtre.dateDeparture == null)) {
                    // Filtre vide ou null => afficher toutes les propriétés
                    modèle.obtenirPropriétés()
                } else {
                    // Appliquer le filtre
                    modèle.rechercherPropriétésParCritères(filtre)
                }

                val proprieteDTOs = filteredProperties.map { mapProprieteADTO(it) }
                withContext(Dispatchers.Main) {
                    if (proprieteDTOs.isNotEmpty()) {
                        view.afficherListPropriete(proprieteDTOs)
                    } else {
                        view.afficherErreur("Aucune propriété trouvée.")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    view.afficherErreur("Erreur : ${e.message}")
                }
            }
        }
    }

    fun afficherResultatsOuProprietes(resultats: ArrayList<ProprieteDTO>?) {
        if (!resultats.isNullOrEmpty()) {
            view.afficherListPropriete(resultats)
        } else {
            accueilDemarrage() // charge la liste non filtrée
        }
    }
    fun accueilDemarrage() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val proprietes = withContext(Dispatchers.IO) {
                    modèle.obtenirPropriétés()  // Liste brute non filtrée
                }
                val proprieteDTOs = proprietes.map { mapProprieteADTO(it) }
                view.afficherListPropriete(proprieteDTOs)
            } catch (e: Exception) {
                e.printStackTrace()
                view.afficherErreur("Erreur lors du chargement des propriétés.")
            }
        }
    }

    fun ProprieteSelectionnéeParId(id: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val propriete = withContext(Dispatchers.IO) {
                    modèle.obtenirProprieteParId(id.toInt())
                }
                if (propriete != null) {
                    view.navigationProprieteDetails()
                } else {
                    view.afficherErreur("Propriété non trouvée pour l'ID: $id")
                }
            } catch (e: Exception) {
                view.afficherErreur("Erreur lors de la récupération de la propriété.")
            }
        }
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
    //format each DateInterval into a string 01-12-2024 to 01-12-2024, 01-12-2024 to 01-12-2024
    private fun formatDateInterval(interval: DateInterval): String {
        val formatter = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault())
        val startDate = java.util.Date(interval.fromDate)
        val endDate = java.util.Date(interval.toDate)
        return "${formatter.format(startDate)} to ${formatter.format(endDate)}"
    }

    fun onRoomSelected() {
        view.navigationProprieteDetails()
    }

    fun navigation() {
        view.navigationFilterEcran()
    }
}
