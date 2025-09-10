package com.example.firebnb.sourceDeDonnees

import android.icu.util.DateInterval
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.firebnb.domaine.entité.Propriete
import com.example.firebnb.domaine.entité.Reservation
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
class SourceBidon : SourceDeDonnées {
    private var proprieteSelectionnee: Propriete? = null
    @RequiresApi(Build.VERSION_CODES.O)
    private val réservations = mutableListOf<Reservation>(
        Reservation(
            id = 1,
            proprieteId = 101, // Identifiant de la propriété associé (à adapter si nécessaire)
            dateArrivee = LocalDate.of(2025, 1, 15), // 15 Janvier 2025
            dateSortie = LocalDate.of(2025, 1, 18), // 18 Janvier 2025
            dateReservation = LocalDate.of(2025, 1, 1), // Date de réservation (exemple)
            prix = 450.0, // 150.0 € par nuit * 3 nuits
            status = Reservation.Status.COMPLÉTÉ,
            titre = "Appartement cosy",
            imageUrl = "room1",
            ville = "Paris",
            pays = "France"
        ),
        Reservation(
            id = 2,
            proprieteId = 102,
            dateArrivee = LocalDate.of(2026, 3, 20),
            dateSortie = LocalDate.of(2026, 3, 25),
            dateReservation = LocalDate.of(2026, 3, 5),
            prix = 750.0, // 150.0 € * 5 nuits
            status = Reservation.Status.COMPLÉTÉ,
            titre = "Chalet montagne",
            imageUrl = "room2",
            ville = "Annecy",
            pays = "France"
        ),
        Reservation(
            id = 3,
            proprieteId = 103,
            dateArrivee = LocalDate.of(2022, 8, 10),
            dateSortie = LocalDate.of(2022, 8, 14),
            dateReservation = LocalDate.of(2022, 7, 25),
            prix = 600.0, // 150.0 € * 4 nuits
            status = Reservation.Status.COMPLÉTÉ,
            titre = "Maison bord de mer",
            imageUrl = "room3",
            ville = "Nice",
            pays = "France"
        ),
        Reservation(
            id = 4,
            proprieteId = 104,
            dateArrivee = LocalDate.of(2025, 2, 13),
            dateSortie = LocalDate.of(2025, 2, 17),
            dateReservation = LocalDate.of(2025, 1, 30),
            prix = 600.0, // 150.0 € * 4 nuits
            status = Reservation.Status.ACCEPTÉ, // "En cours" mappé à "ACCEPTÉ"
            titre = "Grand Appart proche Paris",
            imageUrl = "room4",
            ville = "Neuilly-sur-Marne",
            pays = "France"
        ),
        Reservation(
            id = 5,
            proprieteId = 105,
            dateArrivee = LocalDate.of(2021, 3, 23),
            dateSortie = LocalDate.of(2021, 3, 27),
            dateReservation = LocalDate.of(2021, 3, 10),
            prix = 600.0, // 150.0 € * 4 nuits
            status = Reservation.Status.ACCEPTÉ,
            titre = "Chalet du nord",
            imageUrl = "room5",
            ville = "Saint-Sauveur",
            pays = "Québec"
        ),
        Reservation(
            id = 6,
            proprieteId = 106,
            dateArrivee = LocalDate.of(2023, 3, 4),
            dateSortie = LocalDate.of(2023, 3, 12),
            dateReservation = LocalDate.of(2023, 2, 20),
            prix = 1200.0, // 150.0 € * 8 nuits
            status = Reservation.Status.ACCEPTÉ,
            titre = "Hôtel de luxe",
            imageUrl = "room6",
            ville = "Bali",
            pays = "Indonésie"
        )
    )

    private val proprietes = mutableListOf<Propriete>(
        Propriete(
            id = 1,
            titre = "Chalet Fleurs des Neiges",
            description = "Un chalet confortable au cœur des montagnes.",
            adresse = "123 Rue des Alpes, La Roche, France",
            longitude = 6.4111,
            latitude = 44.7151,
            nbChambre = 3,
            nbSallesDeBain = 2,
            capacitéMax = 6,
            prix = 250.0,
            photo = "room2",
            equipements = arrayListOf("Wi-Fi", "Parking", "Piscine"),
            dateDisponible = arrayListOf(
                DateInterval(1733070000000, 1733329200000)
            ),
            animauxAcceptes = true
        ),
        Propriete(
            id = 2,
            titre = "Petit Appart à Paris",
            description = "Un appartement charmant avec une belle vue.",
            adresse = "456 Rue de Rivoli, Paris, France",
            longitude = 2.3522,
            latitude = 48.8566,
            nbChambre = 2,
            nbSallesDeBain = 1,
            capacitéMax = 4,
            prix = 150.0,
            photo = "room3",
            equipements = arrayListOf("Wi-Fi", "Cuisine", "Balcon"),
            dateDisponible = arrayListOf(
                DateInterval(1733070000000, 1733329200000)
            ),
            animauxAcceptes = false
        ),
        Propriete(
            id = 3,
            titre = "Maison de Campagne",
            description = "Charmante maison au cœur des champs de lavande, idéale pour se détendre.",
            adresse = "12 Rue des Lavandes, Provence, France",
            longitude = 5.9231,
            latitude = 43.6971,
            nbChambre = 4,
            nbSallesDeBain = 3,
            capacitéMax = 8,
            prix = 300.0,
            photo = "room5",
            equipements = arrayListOf("Wi-Fi", "Cheminée", "Terrasse", "Vue sur les montagnes"),
            dateDisponible = arrayListOf(
                DateInterval(1733070000000, 1733329200000)
            ),
            animauxAcceptes = true
        ),
        Propriete(
            id = 4,
            titre = "Appartement Moderne en Centre-ville",
            description = "Appartement moderne avec toutes les commodités, proche des commerces et restaurants.",
            adresse = "34 Boulevard de la Liberté, Lyon, France",
            longitude = 4.8357,
            latitude = 45.7640,
            nbChambre = 2,
            nbSallesDeBain = 1,
            capacitéMax = 4,
            prix = 180.0,
            photo = "room4",
            equipements = arrayListOf("Wi-Fi", "Cuisine équipée", "Balcon"),
            dateDisponible = arrayListOf(
                DateInterval(1733070000000, 1733329200000)
            ),
            animauxAcceptes = false
        ),
        Propriete(
            id = 5,
            titre = "Chalet des Neiges",
            description = "Un chalet chaleureux au pied des pistes, parfait pour les amateurs de ski.",
            adresse = "5 Route des Alpes, Chamonix, France",
            longitude = 6.8694,
            latitude = 45.9237,
            nbChambre = 5,
            nbSallesDeBain = 3,
            capacitéMax = 10,
            prix = 500.0,
            photo = "room7",
            equipements = arrayListOf("Wi-Fi", "Cheminée", "Vue sur les montagnes", "Local à ski"),
            dateDisponible = arrayListOf(
                DateInterval(1733070000000, 1733329200000)
            ),
            animauxAcceptes = true
        ),
        Propriete(
            id = 6,
            titre = "Villa Bord de Mer",
            description = "Magnifique villa avec accès direct à la plage et piscine privée.",
            adresse = "22 Avenue de l'Océan, Biarritz, France",
            longitude = -1.5586,
            latitude = 43.4832,
            nbChambre = 4,
            nbSallesDeBain = 3,
            capacitéMax = 8,
            prix = 1000.0,
            photo = "room8",
            equipements = arrayListOf("Wi-Fi", "Piscine privée", "Vue sur l'océan", "Terrasse"),
            dateDisponible = arrayListOf(
                DateInterval(1733070000000, 1733329200000)
            ),
            animauxAcceptes = true
        ),
        Propriete(
            id = 7,
            titre = "Gîte de Charme",
            description = "Gîte authentique dans une région pittoresque, entouré de verdure.",
            adresse = "89 Rue de la Rivière, Dordogne, France",
            longitude = 0.7194,
            latitude = 45.1839,
            nbChambre = 3,
            nbSallesDeBain = 2,
            capacitéMax = 6,
            prix = 120.0,
            photo = "room9",
            equipements = arrayListOf("Wi-Fi", "Jardin", "Barbecue", "Parking gratuit"),
            dateDisponible = arrayListOf(
                DateInterval(1733070000000, 1733329200000)
            ),
            animauxAcceptes = true
        ),
        Propriete(
            id = 8,
            titre = "Studio Cosy au Cœur de Paris",
            description = "Petit studio charmant avec vue sur les toits de Paris, idéal pour une escapade romantique.",
            adresse = "45 Rue Montmartre, Paris, France",
            longitude = 2.3488,
            latitude = 48.8566,
            nbChambre = 1,
            nbSallesDeBain = 1,
            capacitéMax = 2,
            prix = 90.0,
            photo = "room10",
            equipements = arrayListOf("Wi-Fi", "Cuisine", "Climatisation"),
            dateDisponible = arrayListOf(
                DateInterval(1733070000000, 1733329200000)
            ),
            animauxAcceptes = false
        ),
        Propriete(
            id = 9,
            titre = "Cabane dans les Bois",
            description = "Une cabane rustique entourée de nature, parfaite pour une retraite paisible.",
            adresse = "76 Chemin du Lac, Sologne, France",
            longitude = 1.9887,
            latitude = 47.5339,
            nbChambre = 1,
            nbSallesDeBain = 1,
            capacitéMax = 2,
            prix = 80.0,
            photo = "room11",
            equipements = arrayListOf("Parking", "Vue sur le lac", "Cheminée"),
            dateDisponible = arrayListOf(
                DateInterval(1733070000000, 1733329200000)
            ),
            animauxAcceptes = false
        ),
        Propriete(
            id = 10,
            titre = "Penthouse avec Vue sur la Tour Eiffel",
            description = "Penthouse luxueux avec une vue imprenable sur la Tour Eiffel, parfait pour un séjour haut de gamme.",
            adresse = "7 Avenue des Champs-Élysées, Paris, France",
            longitude = 2.295,
            latitude = 48.8738,
            nbChambre = 3,
            nbSallesDeBain = 2,
            capacitéMax = 6,
            prix = 2000.0,
            photo = "room14",
            equipements = arrayListOf("Wi-Fi", "Balcon", "Service de concierge", "Vue sur la Tour Eiffel"),
            dateDisponible = arrayListOf(
                DateInterval(1733070000000, 1733329200000)
            ),
            animauxAcceptes = false
        ),
        Propriete(
            id = 11,
            titre = "Cottage de la Mer",
            description = "Cottage cosy en bord de mer avec accès direct à la plage.",
            adresse = "54 Rue des Flots, Saint-Malo, France",
            longitude = -2.0275,
            latitude = 48.6493,
            nbChambre = 2,
            nbSallesDeBain = 1,
            capacitéMax = 4,
            prix = 400.0,
            photo = "room12",
            equipements = arrayListOf("Wi-Fi", "Cuisine", "Terrasse", "Vue sur la mer"),
            dateDisponible = arrayListOf(),
            animauxAcceptes = true
        ),
        Propriete(
            id = 12,
            titre = "Maison Provençale",
            description = "Maison traditionnelle provençale avec jardin et piscine.",
            adresse = "9 Rue du Soleil, Aix-en-Provence, France",
            longitude = 5.4474,
            latitude = 43.5297,
            nbChambre = 4,
            nbSallesDeBain = 2,
            capacitéMax = 8,
            prix = 600.0,
            photo = "room13",
            equipements = arrayListOf("Wi-Fi", "Piscine", "Jardin", "Terrasse"),
            dateDisponible = arrayListOf(),
            animauxAcceptes = true
        )
    )


    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun obtenirRéservations(): List<Reservation> {
        delay(100)
        return réservations.toList()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun ajouterRéservation(reservation: Reservation) {
        delay(100)
        Log.d("SourceBidon", "Ajout de la reservation : $reservation")
        réservations.add(0, reservation)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun supprimerRéservation(reservation: Reservation) {
        delay(100)
        réservations.remove(reservation)
    }

    override suspend fun obtenirPropriétés(): List<Propriete> {
        return proprietes.toList()
    }

    override suspend fun trouverPropriété(propriete: Propriete): Propriete? {
        Log.d("sourceBidon", "la propriété recherchée: ${propriete.titre}")
        proprieteSelectionnee = propriete
        Log.d("selectedPropriete", "la propriété sélectionnée: ${proprieteSelectionnee?.titre}")
        return proprieteSelectionnee
    }
    override suspend fun trouverPropriétéParId(id: Int): Propriete? {
        proprieteSelectionnee =  proprietes.find { it.id == id }
        Log.d("selectedPropriete", "la propriété sélectionnée: ${proprieteSelectionnee?.titre}")
        return proprieteSelectionnee
    }

    override suspend fun obtenirPropriétéSélectionnée(): Propriete? {
        Log.d("sourceBidon", "la propriété retournée vers DetailVue: ${proprieteSelectionnee?.titre}")
        return proprieteSelectionnee
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun obtenirRéservationParId(id: String): Reservation? {
        val idInt = id.toIntOrNull() ?: return null
        return réservations.find { it.id == idInt }
    }

    override suspend fun rechercherPropriétésParCritères(
        capacitéMax: Int?,
        prixParNuit: Double?,
        adresse: String?,
        dateDebut: LocalDate?,
        dateFin: LocalDate?
    ): List<Propriete> {
        TODO("Not yet implemented")
    }

}