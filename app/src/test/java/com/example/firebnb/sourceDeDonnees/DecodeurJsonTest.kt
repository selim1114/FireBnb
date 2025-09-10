import com.example.firebnb.sourceDeDonnees.DécodeurJson
import com.example.firebnb.sourceDeDonnees.SourceDeDonnéesException
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import kotlin.test.Test

class DecodeurJsonTest {

    @Test
    fun `Étant donné une propriété valide lorsqu'on la décode on obtient l'objet correspondant`() {
        val json = """
            {
                "id": 1,
                "titre": "Propriété Test",
                "description": "Une propriété de test",
                "adresse": "123 Rue Test",
                "longitude": 45.0,
                "latitude": -75.0,
                "nbChambres": 3,
                "nbSallesDeBain": 2,
                "capaciteMax": 6,
                "prixParNuit": 150.0,
                "photos": ["photo1.jpg", "photo2.jpg"],
                "equipements": ["WiFi", "Piscine"],
                "datesDisponibles": [],
                "animauxAcceptes": true
            }
        """

        val propriete = DécodeurJson.décoderJsonVersPropriete(json)

        assertEquals(1, propriete.id)
        assertEquals("Propriété Test", propriete.titre)
        assertEquals("123 Rue Test", propriete.adresse)
        assertTrue(propriete.animauxAcceptes)
        assertEquals(listOf("WiFi", "Piscine"), propriete.equipements)
    }

    @Test
    fun ` `() {
        val json = """
            [
                {
                    "id": 1,
                    "titre": "Propriété 1",
                    "description": "Description 1",
                    "adresse": "Adresse 1",
                    "longitude": 45.0,
                    "latitude": -75.0,
                    "nbChambres": 3,
                    "nbSallesDeBain": 2,
                    "capaciteMax": 6,
                    "prixParNuit": 150.0,
                    "photos": ["photo1.jpg"],
                    "equipements": ["WiFi"],
                    "datesDisponibles": [],
                    "animauxAcceptes": true
                },
                {
                    "id": 2,
                    "titre": "Propriété 2",
                    "description": "Description 2",
                    "adresse": "Adresse 2",
                    "longitude": 46.0,
                    "latitude": -76.0,
                    "nbChambres": 4,
                    "nbSallesDeBain": 3,
                    "capaciteMax": 8,
                    "prixParNuit": 250.0,
                    "photos": ["photo2.jpg"],
                    "equipements": ["Piscine"],
                    "datesDisponibles": [],
                    "animauxAcceptes": false
                }
            ]
        """
        val proprietes = DécodeurJson.décoderJsonVersListeProprietes(json)

        assertEquals(2, proprietes.size)
        assertEquals("Propriété 1", proprietes[0].titre)
        assertEquals("Propriété 2", proprietes[1].titre)
        assertFalse(proprietes[1].animauxAcceptes)
    }

    @Test
    fun`Étant donné une réservation valide lorsqu'on la décode on obtient une réservation correspondante`() {
        val json = """
        {
            "id": 1,
            "locataire": {
                "id": 1,
                "nom": "Dupont",
                "prénom": "Jean",
                "email": "jean.dupont@example.com",
                "motDePasse": "password123",
                "téléphone": "0123456789",
                "role": "LOCATAIRE",
                "scoreSatisfaction": 4.5,
                "anneeNaissance": 1985,
                "sexe": "HOMME",
                "dateCréation": "2024-12-07"
            },
            "propriété": {
                "id": 1,
                "titre": "Appartement Paris Centre",
                "description": "Appartement de charme au coeur de Paris, avec une vue magnifique.",
                "adresse": "10 Rue de la Paix, Paris",
                "nbChambres": 2,
                "nbSallesDeBain": 1,
                "capacitéMax": 4,
                "prixParNuit": 100.0,
                "photos": [],
                "equipements": [],
                "datesDisponibles": [],
                "animauxAcceptes": true,
                "propriétaire": {
                    "id": 2,
                    "nom": "Martin",
                    "prénom": "Marie",
                    "email": "marie.martin@example.com",
                    "motDePasse": "password123",
                    "téléphone": "0987654321",
                    "role": "PROPRIÉTAIRE",
                    "scoreSatisfaction": 4.8,
                    "anneeNaissance": 1990,
                    "sexe": "FEMME",
                    "dateCréation": "2024-12-07"
                }
            },
            "dateArrive": "2024-12-15",
            "dateSortie": "2024-12-20",
            "dateReservation": "2024-12-01",
            "prix": 500.0,
            "status": "EN_ATTENTE"
        }
    """

        val reservation = DécodeurJson.décoderJsonVersReservation(json)

        assertEquals(1, reservation.id)
        assertEquals(LocalDate.of(2024, 12, 15), reservation.dateArrivee)
        assertEquals(LocalDate.of(2024, 12, 20), reservation.dateSortie)
        assertEquals(500.0, reservation.prix)
        assertEquals("EN_ATTENTE", reservation.status.name)
    }

    @Test
    fun `Lorsque nous décodons une liste de réservations valide, alors une liste correcte est retournée`() {

        val json = """
        [
            {
                "id": 1,
                "locataire": {
                    "id": 1,
                    "nom": "Dupont",
                    "prénom": "Jean",
                    "email": "jean.dupont@example.com",
                    "motDePasse": "password123",
                    "téléphone": "0123456789",
                    "role": "LOCATAIRE",
                    "scoreSatisfaction": 4.5,
                    "anneeNaissance": 1985,
                    "sexe": "FEMME",
                    "dateCréation": "2024-12-07"
                },
                "propriété": {
                    "id": 1,
                    "titre": "Appartement Paris Centre",
                    "description": "Appartement de charme au coeur de Paris, avec une vue magnifique.",
                    "adresse": "10 Rue de la Paix, Paris",
                    "nbChambres": 2,
                    "nbSallesDeBain": 1,
                    "capacitéMax": 4,
                    "prixParNuit": 100.0,
                    "photos": [
                        "https://firebnbimages.s3.us-east-2.amazonaws.com/room4.jpg"
                    ],
                    "equipements": [],
                    "datesDisponibles": [
                        {
                            "dateDebut": "2024-11-30",
                            "dateFin": "2024-12-09"
                        }
                    ],
                    "animauxAcceptes": true,
                    "propriétaire": {
                        "id": 2,
                        "nom": "Martin",
                        "prénom": "Marie",
                        "email": "marie.martin@example.com",
                        "motDePasse": "password123",
                        "téléphone": "0987654321",
                        "role": "PROPRIÉTAIRE",
                        "scoreSatisfaction": 4.8,
                        "anneeNaissance": 1990,
                        "sexe": "FEMME",
                        "dateCréation": "2024-12-07"
                    }
                },
                "dateArrive": "2024-12-15",
                "dateSortie": "2024-12-20",
                "dateReservation": "2024-12-01",
                "prix": 500.0,
                "status": "EN_ATTENTE"
            },
            {
                "id": 9,
                "locataire": {
                    "id": 1,
                    "nom": "Dupont",
                    "prénom": "Jean",
                    "email": "jean.dupont@example.com",
                    "motDePasse": "password123",
                    "téléphone": "0123456789",
                    "role": "LOCATAIRE",
                    "scoreSatisfaction": 4.5,
                    "anneeNaissance": 1985,
                    "sexe": "FEMME",
                    "dateCréation": "2024-12-07"
                },
                "propriété": {
                    "id": 1,
                    "titre": "Appartement Paris Centre",
                    "description": "Appartement de charme au coeur de Paris, avec une vue magnifique.",
                    "adresse": "10 Rue de la Paix, Paris",
                    "nbChambres": 2,
                    "nbSallesDeBain": 1,
                    "capacitéMax": 4,
                    "prixParNuit": 100.0,
                    "photos": [
                        "https://firebnbimages.s3.us-east-2.amazonaws.com/room4.jpg"
                    ],
                    "equipements": [],
                    "datesDisponibles": [
                        {
                            "dateDebut": "2024-11-30",
                            "dateFin": "2024-12-09"
                        }
                    ],
                    "animauxAcceptes": true,
                    "propriétaire": {
                        "id": 2,
                        "nom": "Martin",
                        "prénom": "Marie",
                        "email": "marie.martin@example.com",
                        "motDePasse": "password123",
                        "téléphone": "0987654321",
                        "role": "PROPRIÉTAIRE",
                        "scoreSatisfaction": 4.8,
                        "anneeNaissance": 1990,
                        "sexe": "FEMME",
                        "dateCréation": "2024-12-07"
                    }
                },
                "dateArrive": "2024-12-20",
                "dateSortie": "2024-12-27",
                "dateReservation": "2024-10-25",
                "prix": 840.0,
                "status": "EN_ATTENTE"
            }
        ]
    """
        val reservations = DécodeurJson.décoderJsonVersListeReservations(json)

        assertEquals(2, reservations.size)

        val reservation1 = reservations[0]
        assertEquals(1, reservation1.id)
        assertEquals(LocalDate.of(2024, 12, 15), reservation1.dateArrivee)
        assertEquals(LocalDate.of(2024, 12, 20), reservation1.dateSortie)
        assertEquals(500.0, reservation1.prix)
        assertEquals("EN_ATTENTE", reservation1.status.name)

        val reservation2 = reservations[1]
        assertEquals(9, reservation2.id)
        assertEquals(LocalDate.of(2024, 12, 20), reservation2.dateArrivee)
        assertEquals(LocalDate.of(2024, 12, 27), reservation2.dateSortie)
        assertEquals(840.0, reservation2.prix)
        assertEquals("EN_ATTENTE", reservation2.status.name)
    }
    @Test
    fun `Étant donné un JSON invalide lorsqu'on essaie de le décoder alors une exception est levée`() {
        val json = """
        {
            "id": ,
            "titre": "Propriété Test",
            "description": "Une propriété de test",
            "adresse": "123 Rue Test"
    """

        val exception = assertThrows<SourceDeDonnéesException> {
            DécodeurJson.décoderJsonVersPropriete(json)
        }

        assertEquals("Format JSON invalide", exception.message)
    }
}
