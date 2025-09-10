package com.example.firebnb.Services

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.firebnb.domaine.entité.Propriete
import com.example.firebnb.sourceDeDonnees.SourceDeDonnées
import java.time.LocalDate

class PropriétéService (var source : SourceDeDonnées) {

    suspend fun obtenirPropriétés(): List<Propriete> {
        return source.obtenirPropriétés()
    }

    suspend fun obtenirPropriétéSélectionnée(): Propriete? {
        val propriete = source.obtenirPropriétéSélectionnée()
        Log.d("Service", "Propriete recu service: ${propriete?.titre}")
        return propriete
    }
    suspend fun obtenirProprieteParId(id: Int): Propriete? {
        return source.trouverPropriétéParId(id)
    }
    suspend fun rechercherParCritères(capacitéMax: Int?, prixParNuit: Double?, adresse: String?, dateDebut: LocalDate?, dateFin: LocalDate?): List<Propriete> {
        return source.rechercherPropriétésParCritères(capacitéMax, prixParNuit, adresse, dateDebut, dateFin)
    }
}