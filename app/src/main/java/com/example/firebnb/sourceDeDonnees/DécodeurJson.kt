package com.example.firebnb.sourceDeDonnees

import android.icu.util.DateInterval
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.firebnb.domaine.entité.*
import com.example.firebnb.domaine.entité.Propriete
import com.example.firebnb.domaine.entité.Reservation
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.MalformedJsonException
import java.io.StringReader
import java.io.EOFException
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class DécodeurJson {

    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        @Throws(SourceDeDonnéesException::class)
        fun décoderJsonVersPropriete(json: String): Propriete {
            val reader = JsonReader(StringReader(json))
            try {
                return décoderPropriete(reader)
            } catch (exc: EOFException) {
                throw SourceDeDonnéesException("Format JSON invalide")
            } catch (exc: MalformedJsonException) {
                throw SourceDeDonnéesException("Format JSON invalide")
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        @Throws(SourceDeDonnéesException::class)
        fun décoderJsonVersListeProprietes(json: String): List<Propriete> {
            val reader = JsonReader(StringReader(json))
            val proprietes = mutableListOf<Propriete>()
            try {
                reader.beginArray()
                while (reader.hasNext()) {
                    proprietes.add(décoderPropriete(reader))
                }
                reader.endArray()
            } catch (exc: EOFException) {
                throw SourceDeDonnéesException("Format JSON invalide")
            } catch (exc: MalformedJsonException) {
                throw SourceDeDonnéesException("Format JSON invalide")
            }
            return proprietes
        }

        @RequiresApi(Build.VERSION_CODES.O)
        @Throws(SourceDeDonnéesException::class)
        fun décoderJsonVersReservation(json: String): Reservation {
            val reader = JsonReader(StringReader(json))
            try {
                return décoderReservation(reader)
            } catch (exc: EOFException) {
                throw SourceDeDonnéesException("Format JSON invalide")
            } catch (exc: MalformedJsonException) {
                throw SourceDeDonnéesException("Format JSON invalide")
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        @Throws(SourceDeDonnéesException::class)
        fun décoderJsonVersListeReservations(json: String): List<Reservation> {
            val reader = JsonReader(StringReader(json))
            val reservations = mutableListOf<Reservation>()
            try {
                reader.beginArray()
                while (reader.hasNext()) {
                    reservations.add(décoderReservation(reader))
                }
                reader.endArray()
            } catch (exc: EOFException) {
                throw SourceDeDonnéesException("Format JSON invalide")
            } catch (exc: MalformedJsonException) {
                throw SourceDeDonnéesException("Format JSON invalide")
            }
            return reservations
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun décoderPropriete(reader: JsonReader): Propriete {
            var id: Int = 0
            var titre: String = ""
            var description: String = ""
            var adresse: String = ""
            var longitude: Double = 0.0
            var latitude: Double = 0.0
            var nbChambre: Int = 0
            var nbSallesDeBain: Int = 0
            var capaciteMax: Int = 0
            var prix: Double = 0.0
            var photo: String = ""
            var equipements = arrayListOf<String>()
            var dateDisponible = arrayListOf<DateInterval>()
            var animauxAcceptes: Boolean = false

            reader.beginObject()
            while (reader.hasNext()) {
                when (reader.nextName()) {
                    "id" -> id = reader.nextInt()
                    "titre" -> titre = reader.nextString()
                    "description" -> description = reader.nextString()
                    "adresse" -> adresse = reader.nextString()
                    "longitude" -> longitude = reader.nextDouble()
                    "latitude" -> latitude = reader.nextDouble()
                    "nbChambres" -> nbChambre = reader.nextInt()
                    "nbSallesDeBain" -> nbSallesDeBain = reader.nextInt()
                    "capacitéMax" -> capaciteMax = reader.nextInt()
                    "prixParNuit" -> prix = reader.nextDouble()
                    "photos" -> {
                        reader.beginArray()
                        while (reader.hasNext()) {
                            photo = reader.nextString()
                        }
                        reader.endArray()
                    }
                    "equipements" -> equipements = décoderArrayString(reader)
                    "datesDisponibles" -> dateDisponible = décoderArrayDateInterval(reader)
                    "animauxAcceptes" -> animauxAcceptes = reader.nextBoolean()
                    else -> reader.skipValue()
                }
            }
            reader.endObject()

            return Propriete( id, titre, description, adresse, longitude, latitude, nbChambre, nbSallesDeBain,
                capaciteMax, prix, photo, equipements, dateDisponible, animauxAcceptes )
        }
        @RequiresApi(Build.VERSION_CODES.O)
        private fun décoderReservation(reader: JsonReader): Reservation {
            var id: Int = 0
            var propriete: Propriete? = null
            var dateArrivee: LocalDate? = null
            var dateSortie: LocalDate? = null
            var dateReservation: LocalDate? = null
            var prix: Double = 0.0
            var status: Reservation.Status = Reservation.Status.EN_ATTENTE

            reader.beginObject()
            while (reader.hasNext()) {
                when (val name = reader.nextName()) {
                    "id" -> id = reader.nextInt()
                    "propriété" -> propriete = décoderPropriete(reader)
                    "dateArrive" -> dateArrivee = LocalDate.parse(reader.nextString())
                    "dateSortie" -> dateSortie = LocalDate.parse(reader.nextString())
                    "dateReservation" -> dateReservation = LocalDate.parse(reader.nextString())
                    "prix" -> prix = reader.nextDouble()
                    "status" -> status = Reservation.Status.valueOf(reader.nextString())
                    "locataire" -> reader.skipValue()
                    else -> reader.skipValue()
                }
            }
            reader.endObject()

            if (propriete == null || dateArrivee == null || dateSortie == null || dateReservation == null) {
                throw IllegalArgumentException("Données invalides ou manquantes dans la réservation")
            }

            val reservation = Reservation(
                id = id,
                proprieteId = propriete.id,
                dateArrivee = dateArrivee,
                dateSortie = dateSortie,
                dateReservation = dateReservation,
                prix = prix,
                status = status,
                titre = propriete.titre,
                imageUrl = propriete.photo,
                ville = propriete.adresse
            )
       return reservation
        }

        private fun décoderArrayString(reader: JsonReader): ArrayList<String> {
            val list = arrayListOf<String>()
            reader.beginArray()
            while (reader.hasNext()) {
                list.add(reader.nextString())
            }
            reader.endArray()
            return list
        }
        @RequiresApi(Build.VERSION_CODES.O)
        private fun lireDate(reader: JsonReader): Long {
            return if (reader.peek() == JsonToken.STRING) {
                val dateString = reader.nextString()
                val localDate = LocalDate.parse(dateString)
                localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            } else if (reader.peek() == JsonToken.NUMBER) {
                reader.nextLong()
            } else {
                throw IllegalArgumentException("Date format not supported")
            }
        }
        @RequiresApi(Build.VERSION_CODES.O)
        private fun décoderArrayDateInterval(reader: JsonReader): ArrayList<DateInterval> {
            val intervals = arrayListOf<DateInterval>()

            reader.beginArray()
            while (reader.hasNext()) {
                reader.beginObject()
                var dateDebut: Long? = null
                var dateFin: Long? = null
                while (reader.hasNext()) {
                    when (reader.nextName()) {
                        "dateDebut" -> dateDebut = lireDate(reader)
                        "dateFin" -> dateFin = lireDate(reader)
                        else -> reader.skipValue()
                    }
                }
                reader.endObject()

                if (dateDebut != null && dateFin != null) {
                    intervals.add(DateInterval(dateDebut, dateFin))
                }
            }
            reader.endArray()

            return intervals
        }
    }
}