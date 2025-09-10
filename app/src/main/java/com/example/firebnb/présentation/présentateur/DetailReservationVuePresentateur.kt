package com.example.firebnb.présentation.présentateur

import android.content.Context
import com.example.firebnb.présentation.modèle.ModèleImpl
import com.example.firebnb.présentation.vue.ReservationDetailVue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailReservationVuePresentateur(

    private val vue: ReservationDetailVue,
    private val context: Context
) {
    var modele = ModèleImpl.instance
    fun chargerDetailsReservation(reservationId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val reservation = modele.obtenirReservationParId(reservationId)
                withContext(Dispatchers.Main) {
                    if (reservation != null) {
                        vue.afficherDetails(reservation)
                    } else {
                        vue.afficherErreur("Réservation non trouvée")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    vue.afficherErreur("Erreur lors du chargement de la réservation : ${e.message}")
                }
            }
        }
    }

    fun supprimerReservation(reservationId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val reservation = modele.obtenirReservationParId(reservationId)
                if (reservation != null) {
                    modele.supprimerRéservation(reservation)
                    withContext(Dispatchers.Main) {
                        vue.afficherMessage("Réservation supprimée avec succès.")
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        vue.afficherErreur("Réservation non trouvée.")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    vue.afficherErreur("Erreur lors de la suppression : ${e.message}")
                }
            }
        }
    }
}