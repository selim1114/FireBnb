package com.example.firebnb.présentation.présentateur

import android.icu.util.DateInterval
import android.util.Log
import com.example.firebnb.domaine.entité.Propriete
import com.example.firebnb.présentation.ProprieteDTO
import com.example.firebnb.présentation.vue.ProprieteFavorisVue
import com.example.firebnb.sourceDeDonnees.DatabaseHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavorisPresentateur(
    private val view: ProprieteFavorisVue
) {
    private lateinit var databaseHelper: DatabaseHelper

    fun getDatabaseHelper() {
        databaseHelper = DatabaseHelper(view.getDatabaseHelper())
    }

    fun accueilDemarrage() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                withContext(Dispatchers.IO) {
                    databaseHelper.supprimerLesDoublons()
                }
                val favoris = withContext(Dispatchers.IO) {
                    databaseHelper.obtenirsFavoris()
                }
                val favorisDTOs = favoris.map { mapProprieteADTO(it) }
                view.afficherFavoris(favorisDTOs)
            } catch (e: Exception) {
                Log.e("AccueilPrésentateur", "Erreur lors du chargement des favoris", e)
                view.afficherErreur("Erreur lors du chargement des favoris : ${e.message}")
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
    fun supprimerFavoris(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                databaseHelper.supprimerFavoris(id)
                withContext(Dispatchers.Main) {
                    view.notifierItemSupprimé(id)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    view.afficherErreur("Erreur lors de la suppression : ${e.message}")
                }
            }
        }
    }
    //format each DateInterval into a string 01-12-2024 to 01-12-2024, 01-12-2024 to 01-12-2024
    private fun formatDateInterval(interval: DateInterval): String {
        val formatter = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault())
        val startDate = java.util.Date(interval.fromDate)
        val endDate = java.util.Date(interval.toDate)
        return "${formatter.format(startDate)} to ${formatter.format(endDate)}"
    }
}
