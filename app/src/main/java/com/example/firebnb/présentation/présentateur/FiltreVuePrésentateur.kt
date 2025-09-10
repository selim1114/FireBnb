package com.example.firebnb.présentation.présentateur

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.firebnb.domaine.entité.FiltreClass
import com.example.firebnb.domaine.entité.Propriete
import com.example.firebnb.présentation.ProprieteDTO
import com.example.firebnb.présentation.modèle.ModèleImpl
import com.example.firebnb.présentation.vue.FiltreVue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FiltreVuePrésentateur(
    private val view: FiltreVue
) {
    private val modèle = ModèleImpl.instance

    @RequiresApi(Build.VERSION_CODES.O)
    fun appliquerFiltre(filtre: FiltreClass) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val filteredProperties = modèle.rechercherPropriétésParCritères(filtre)
                val filtredProprieteDTO = filteredProperties.map { mapProprieteADTO(it) }

                withContext(Dispatchers.Main) {
                    if (filteredProperties.isNotEmpty()) {
                        view.naviguerAvecRésultats(filtredProprieteDTO)
                    } else {
                        view.afficherErreur("Aucune propriété trouvée pour ces critères.")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    view.afficherErreur("Erreur lors de l'application des filtres : ${e.message}")
                }
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
            dateDisponible = propriete.dateDisponible.joinToString(";") {
                "${formatDate(it.fromDate)} - ${formatDate(it.toDate)}"
            },
            animauxAcceptes = propriete.animauxAcceptes
        )
    }

    private fun formatDate(date: Long): String {
        val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return formatter.format(Date(date))
    }
}
