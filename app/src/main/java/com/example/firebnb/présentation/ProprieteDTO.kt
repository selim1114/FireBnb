package com.example.firebnb.présentation


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProprieteDTO(
    val id: String,
    val titre: String,
    val adresse: String,
    val longitude: String,
    val latitude: String,
    val description: String,
    val prix: String,
    val nbChambre: String,
    val capaciteMax: String,
    val nbSallesDeBain: String,
    val photo: String,
    val equipements: String,
    val dateDisponible: String,
    val animauxAcceptes: Boolean
) : Parcelable
