package com.example.firebnb.sourceDeDonnees

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.firebnb.domaine.entité.Propriete
import com.example.firebnb.domaine.entité.Reservation
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Headers
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.net.URLEncoder
import java.time.LocalDate

class SourceDeDonnéesHttp(private val urls: Map<String, String>) :SourceDeDonnées{

    private val client = OkHttpClient()
    private var proprieteSelectionnee: Propriete? = null
    val token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6InFMRGZsaHd5bWZWR1cwN3FmVzJ0NCJ9.eyJodHRwczovL2ZpcmVibmIuY29tL3JvbGVzIjpbIlJPTEVfTG9jYXRhaXJlIl0sImh0dHBzOi8vZmlyZWJuYi5jb20vZW1haWwiOiJqZWFuLmR1cG9udEBleGFtcGxlLmNvbSIsImlzcyI6Imh0dHBzOi8vZGV2LThkZGRwdGppbmNoejE2YWkudXMuYXV0aDAuY29tLyIsInN1YiI6ImF1dGgwfDY3NTQ1Zjc2ZTg4NWI5MzRlN2UyNWI4NSIsImF1ZCI6WyJodHRwczovL2ZpcmVibmIuY29tIiwiaHR0cHM6Ly9kZXYtOGRkZHB0amluY2h6MTZhaS51cy5hdXRoMC5jb20vdXNlcmluZm8iXSwiaWF0IjoxNzM0NjU1NDgxLCJleHAiOjE3MzQ3NDE4ODEsInNjb3BlIjoib3BlbmlkIHByb2ZpbGUgZW1haWwgYWRkcmVzcyBwaG9uZSIsImd0eSI6InBhc3N3b3JkIiwiYXpwIjoieDlkdmFoRjhUWVVuVkU4VHlETG9FYnVoSFNJVDFUeEwiLCJwZXJtaXNzaW9ucyI6WyJjcsOpZXI6cmVzZXJ2YXRpb24iLCJtb2RpZmllcjpwcm9maWwiXX0.n7L9AApF-XVMyj_qqJIMB_UdSU-vLsaq2cWVimK-RsH8mrp7NfiWSXg72eQeZ4o4mh5R4V6OvW4CgkOOwRyLoAfCb0Z-6TlaDqaNex_sJS6QNA69QGw0oo5QILDvdWjRXqBdt8-xHg9Yc3iQGKMzCSa-SEhYT5MaKAufH0mESOCGhzOCC3vyCogcxMysV3PAUhT2Fe63Q_mr2wzXyVUxfz7_KajYoBWauktIMrgNUGAf5MeHqJ0739FnzEpAx4o6UIgXvc3pWizz53MErfrfDFtXzmOxLMbt8WHJ8vjYFuOL5DzgTjPIAIYpucTs1WjEvSIpw66DBXlVZvktZ9uVSg"

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun obtenirRéservations(): List<Reservation> {
        try {
            val headers = Headers.Builder()
                .add("Authorization", "Bearer " + token)
                .build()

            val request = Request.Builder()
                .url(urls["url_reservations"]!!)
                .headers( headers )
                .get()
                .build()

            val response = client.newCall(request).execute()

            if (response.code != 200) {
                throw SourceDeDonnéesException("Erreur : ${response.code}")
            }

            val responseBody = response.body?.string()
                ?: throw SourceDeDonnéesException("Pas de données reçues")

            return DécodeurJson.décoderJsonVersListeReservations(responseBody)
        } catch (e: IOException) {
            throw SourceDeDonnéesException(e.message ?: "Erreur inconnue")
        }
    }

    override suspend fun ajouterRéservation(reservation: Reservation) {
        TODO("Not yet implemented")
    }

    override suspend fun supprimerRéservation(reservation: Reservation) {
        try {
            val url = "${urls["url_reservations"]}/${reservation.id}"
            val headers = Headers.Builder()
                .add("Authorization", "Bearer " + token)
                .build()
            val request = Request.Builder()
                .url(url)
                .headers(headers)
                .delete()
                .build()

            val response = client.newCall(request).execute()

            if (response.code != 204) {
                throw SourceDeDonnéesException("Erreur lors de la suppression : ${response.code}")
            }
        } catch (e: IOException) {
            throw SourceDeDonnéesException(e.message ?: "Erreur inconnue")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Throws(SourceDeDonnéesException::class)
    override suspend fun obtenirPropriétés(): List<Propriete> {
        try {

            val headers = Headers.Builder()
                .add("Authorization", "Bearer " + token)
                .build()
            val request = Request.Builder()
                .url(urls["url_proprietes"]!!)
                .headers( headers )
                .get()
                .build()

            val response = client.newCall(request).execute()

            if (response.code != 200) {
                throw SourceDeDonnéesException("Erreur : ${response.code}")
            }

            val responseBody = response.body?.string()
                ?: throw SourceDeDonnéesException("Pas de données reçues")

            return DécodeurJson.décoderJsonVersListeProprietes(responseBody)
        } catch (e: IOException) {
            throw SourceDeDonnéesException(e.message ?: "Erreur inconnue")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun trouverPropriétéParId(id: Int): Propriete? {
        try {
            val url = "${urls["url_proprietes"]}/$id"
            val headers = Headers.Builder()
                .add("Authorization", "Bearer " + token)
                .build()
            val request = Request.Builder()
                .url(url)
                .headers(headers)
                .get()
                .build()

            val response = client.newCall(request).execute()

            if (response.code == 404) {
                return null
            } else if (response.code != 200) {
                throw SourceDeDonnéesException("Erreur : ${response.code}")
            }

            val responseBody = response.body?.string()
                ?: throw SourceDeDonnéesException("Pas de données reçues")

            val propriete = DécodeurJson.décoderJsonVersPropriete(responseBody)
            proprieteSelectionnee = propriete
            return proprieteSelectionnee
        } catch (e: IOException) {
            throw SourceDeDonnéesException(e.message ?: "Erreur inconnue")
        }
    }

    override suspend fun trouverPropriété(propriete: Propriete): Propriete? {
        proprieteSelectionnee = propriete
        return proprieteSelectionnee    }

    override suspend fun obtenirPropriétéSélectionnée(): Propriete? {
        return proprieteSelectionnee
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun obtenirRéservationParId(id: String): Reservation? {
        try {
            val url = "${urls["url_reservations"]}/$id"
            val headers = Headers.Builder()
                .add("Authorization", "Bearer " + token)
                .build()
            val request = Request.Builder()
                .url(url)
                .headers(headers)
                .get()
                .build()

            val response = client.newCall(request).execute()

            if (response.code == 404) {
                return null
            } else if (response.code != 200) {
                throw SourceDeDonnéesException("Erreur : ${response.code}")
            }

            val responseBody = response.body?.string()
                ?: throw SourceDeDonnéesException("Pas de données reçues")

            return DécodeurJson.décoderJsonVersReservation(responseBody)
        } catch (e: IOException) {
            throw SourceDeDonnéesException(e.message ?: "Erreur inconnue")
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun rechercherPropriétésParCritères(
        capacitéMax: Int?,
        prixParNuit: Double?,
        adresse: String?,
        dateDebut: LocalDate?,
        dateFin: LocalDate?
    ): List<Propriete> {
        try {
            // Construire le chemin de base sans encodage
            val baseUrl = "http://idefix.dti.crosemont.quebec:9048/api/propriétés/recherche"

            // Ajouter les paramètres encodés manuellement
            val queryParams = mutableListOf<String>()
            capacitéMax?.let { queryParams.add("capacitéMax=$it") }
            prixParNuit?.let { queryParams.add("prixParNuit=$it") }
            adresse?.let { queryParams.add("adresse=${URLEncoder.encode(it, "UTF-8")}") }
            dateDebut?.let { queryParams.add("dateDebut=$it") }
            dateFin?.let { queryParams.add("dateFin=$it") }

            // Concaténer l'URL finale
            val finalUrl = "$baseUrl?${queryParams.joinToString("&")}"

            // Log de l'URL finale
            Log.d("SourceDeDonnéesHttp", "Requête URL : $finalUrl")

            // Construire la requête
            val headers = Headers.Builder()
                .add("Authorization", "Bearer $token")
                .build()

            val request = Request.Builder()
                .url(finalUrl)
                .headers(headers)
                .get()
                .build()

            // Exécuter la requête
            val response = client.newCall(request).execute()

            // Gérer les erreurs de la réponse
            if (response.code != 200) {
                val errorMessage = response.body?.string()
                Log.e("SourceDeDonnéesHttp", "Erreur HTTP ${response.code} : $errorMessage")
                throw SourceDeDonnéesException("Erreur API : ${response.code}")
            }

            val responseBody = response.body?.string()
                ?: throw SourceDeDonnéesException("Pas de données reçues")

            return DécodeurJson.décoderJsonVersListeProprietes(responseBody)
        } catch (e: IOException) {
            throw SourceDeDonnéesException(e.message ?: "Erreur inconnue")
        }
    }
}