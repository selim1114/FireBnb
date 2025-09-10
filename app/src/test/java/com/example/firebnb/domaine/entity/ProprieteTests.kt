package com.example.firebnb.domaine.entity

import android.icu.util.DateInterval
import com.example.firebnb.domaine.entité.Propriete
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

class ProprieteTests {

    @Test
    fun `Étant donné une propriété avec des données valides, lorsqu'on la crée, ses attributs sont correctement définis`() {
        val equipements = arrayListOf("WiFi", "Piscine")
        val datesDisponibles = listOf(
            DateInterval(1672444800000, 1673654400000)
        )

        val propriete = Propriete(
            id = 1,
            titre = "Belle Maison",
            description = "Maison spacieuse et lumineuse",
            adresse = "123 rue de Paris",
            longitude = 2.3522,
            latitude = 48.8566,
            nbChambre = 3,
            nbSallesDeBain = 3,
            capacitéMax = 5,
            prix = 150.0,
            photo = "photo_maison.jpg",
            equipements = equipements,
            dateDisponible = datesDisponibles,
            animauxAcceptes = false
        )

        assertEquals(1, propriete.id)
        assertEquals("Belle Maison", propriete.titre)
        assertEquals("123 rue de Paris", propriete.adresse)
        assertEquals(2.3522, propriete.longitude, 0.0)
        assertEquals(48.8566, propriete.latitude, 0.0)
        assertEquals("Maison spacieuse et lumineuse", propriete.description)
        assertEquals(150.0, propriete.prix, 0.0)
        assertEquals(3, propriete.nbChambre)
        assertEquals("photo_maison.jpg", propriete.photo)
        assertEquals(equipements, propriete.equipements)
        assertEquals(datesDisponibles, propriete.dateDisponible)
        assertEquals(5, propriete.capacitéMax)
        assertEquals(3, propriete.nbSallesDeBain)
        assertEquals(false, propriete.animauxAcceptes)

    }


    @Test
    fun `Étant donné une propriété avec un prix négatif, lorsqu'on essaie de créer la propriété, on obtient une exception`() {
        val equipements = arrayListOf<String>()
        val datesDisponibles = emptyList<DateInterval>()

        try {
            Propriete(
                id = 2,
                titre = "Maison avec prix négatif",
                description = "Description inutile",
                adresse = "123 rue inconnue",
                longitude = 0.0,
                latitude = 0.0,
                nbChambre = 1,
                nbSallesDeBain = 1,
                capacitéMax = 2,
                prix = -50.0,
                photo = "photo.jpg",
                equipements = equipements,
                dateDisponible = datesDisponibles,
                animauxAcceptes = true
            )
            fail("Une exception aurait dû être levée pour un prix négatif.")
        } catch (e: IllegalArgumentException) {
            assertEquals("Le prix de la propriété ne peut pas être négatif", e.message)
        }
    }

    @Test
    fun `Étant donné une propriété avec un nombre de chambres négatif, lorsqu'on essaie de créer la propriété, on obtient une exception`() {
        try {
            Propriete(
                id = 3,
                titre = "Appartement cosy",
                description = "Un appartement charmant",
                adresse = "123 Rue du Soleil",
                longitude = 2.3522,
                latitude = 48.8566,
                nbChambre = -1,
                nbSallesDeBain = 1,
                capacitéMax = 2,
                prix = 150.0,
                photo = "image.jpg",
                equipements = arrayListOf("WiFi"),
                dateDisponible = emptyList(),
                animauxAcceptes = false
            )
            fail("Une exception aurait dû être levée pour un nombre de chambres négatif.")
        } catch (e: IllegalArgumentException) {
            assertEquals("Le nombre de chambres ne peut pas être négatif", e.message)
        }
    }

    @Test
    fun `Étant donné une propriété avec un titre vide, lorsqu'on essaie de créer la propriété, on obtient une exception`() {
        try {
            Propriete(
                id = 4,
                titre = "",
                description = "Description inutile",
                adresse = "123 Rue inconnue",
                longitude = 0.0,
                latitude = 0.0,
                nbChambre = 1,
                nbSallesDeBain = 1,
                capacitéMax = 2,
                prix = 100.0,
                photo = "image.jpg",
                equipements = arrayListOf("WiFi"),
                dateDisponible = emptyList(),
                animauxAcceptes = false
            )
            fail("Une exception aurait dû être levée pour un titre vide.")
        } catch (e: IllegalArgumentException) {
            assertEquals("Le titre ne peut pas être vide", e.message)
        }
    }

    @Test
    fun `Étant donné une propriété avec une adresse vide, lorsqu'on essaie de créer la propriété, on obtient une exception`() {
        try {
            Propriete(
                id = 5,
                titre = "Appartement moderne",
                description = "Un appartement charmant",
                adresse = "",
                longitude = 2.3522,
                latitude = 48.8566,
                nbChambre = 2,
                nbSallesDeBain = 1,
                capacitéMax = 4,
                prix = 200.0,
                photo = "image.jpg",
                equipements = arrayListOf(),
                dateDisponible = emptyList(),
                animauxAcceptes = true
            )
            fail()
        } catch (e: IllegalArgumentException) {
            assertEquals("L'adresse ne peut pas être vide", e.message)
        }
    }
    @Test
    fun `Étant donné une propriété avec une photo vide, lorsqu'on essaie de créer la propriété, on obtient une exception`() {
        try {
            Propriete(
                id = 5,
                titre = "Appartement moderne",
                description = "Un appartement charmant",
                adresse = "test",
                longitude = 2.3522,
                latitude = 48.8566,
                nbChambre = 2,
                nbSallesDeBain = 1,
                capacitéMax = 4,
                prix = 200.0,
                photo = "",
                equipements = arrayListOf("WiFi"),
                dateDisponible = emptyList(),
                animauxAcceptes = true
            )
            fail()
        } catch (e: IllegalArgumentException) {
            assertEquals("La photo ne peut pas être vide", e.message)
        }
    }


}