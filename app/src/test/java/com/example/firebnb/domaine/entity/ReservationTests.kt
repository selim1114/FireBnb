package com.example.firebnb.domaine.entity

import com.example.firebnb.domaine.entité.Reservation
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.fail
import org.junit.Assert.assertThrows
import org.junit.Test
import java.time.LocalDate

class ReservationTests {
    @Test
    fun `Étant donné une réservation valide, lorsqu'on la crée, elle est correctement initialisée`() {
        val reservation = Reservation(
        id = 123,
        proprieteId = 456,
        dateArrivee = LocalDate.of(2025, 1, 15),
        dateSortie = LocalDate.of(2025, 1, 20),
        dateReservation = LocalDate.of(2024, 12, 1),
        prix = 150.0,
        status = Reservation.Status.ACCEPTÉ,
        titre = "Appartement cosy",
        imageUrl = "image.jpg",
        ville = "Paris",
        pays = "France"
        )

        assertEquals(reservation.id, 123)
        assertEquals(reservation.titre , "Appartement cosy")
        assertEquals(reservation.proprieteId, 456)
        assertEquals(LocalDate.of(2025, 1, 15), reservation.dateArrivee)
        assertEquals(LocalDate.of(2025, 1, 20), reservation.dateSortie)
        assertEquals(LocalDate.of(2024, 12, 1), reservation.dateReservation)
        assertEquals(reservation.prix , 150.0)
        assertEquals(reservation.status ,Reservation.Status.ACCEPTÉ)
        assertEquals(reservation.ville,"Paris")
        assertEquals(reservation.pays,"France")
    }

    @Test
    fun `Étant donné une réservation avec un prix négatif, lorsqu'on essaie de créer la réservation, on obtient une exception`() {
        try {
            Reservation(
                id = 124,
                proprieteId = 789,
                dateArrivee = LocalDate.of(2025, 2, 1),
                dateSortie = LocalDate.of(2025, 2, 5),
                dateReservation = LocalDate.of(2024, 12, 10),
                prix = -50.0,
                status = Reservation.Status.EN_ATTENTE,
                titre = "Studio moderne",
                imageUrl = "studio.jpg",
                ville = "Lyon",
                pays = "France"
            )
            fail()
        }catch (e:IllegalArgumentException){
            assertEquals("Le prix de la propriété ne peut pas être négatif", e.message)
        }
    }
    

    @Test
    fun `Étant donné une réservation avec une date d'arrivée après la date de sortie, lorsqu'on essaie de la créer, on obtient une exception`() {
      try{
            Reservation(
                id = 125,
                proprieteId = 789,
                dateArrivee = LocalDate.of(2025, 3, 10),
                dateSortie = LocalDate.of(2025, 3, 5),
                dateReservation = LocalDate.of(2024, 12, 20),
                prix = 100.0,
                status = Reservation.Status.EN_ATTENTE,
                titre = "Maison familiale",
                imageUrl = "maison.jpg",
                ville = "Marseille",
                pays = "France"
            )
          fail()
      }catch (e:IllegalArgumentException){
          assertEquals("vous pouvez pas choisir une date de sortie avant la date d'arrivée",e.message)
      }
    }
}