package com.example.firebnb.présentation.vue

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebnb.R
import com.example.firebnb.domaine.entité.Reservation
import com.example.firebnb.présentation.présentateur.RéservationPrésentateur
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

class ReservationAdapter(
    private val presentateur: RéservationPrésentateur
) : RecyclerView.Adapter<ReservationAdapter.VueHolderReservation>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class VueHolderReservation(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val titre: TextView = itemView.findViewById(R.id.titre)
        private val dateArrivee: TextView = itemView.findViewById(R.id.date_arrivee)
        private val dateSortie: TextView = itemView.findViewById(R.id.date_sortie)
        private val ville: TextView = itemView.findViewById(R.id.ville)
        private val pays: TextView = itemView.findViewById(R.id.pays)
        private val prixTotal: TextView = itemView.findViewById(R.id.prix_total)
        private val etat: TextView = itemView.findViewById(R.id.etat_reservation)
        private val imageEnTete: ImageView = itemView.findViewById(R.id.header_image)

        @RequiresApi(Build.VERSION_CODES.O)
        fun bindItems(reservation: Reservation) {
            val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault())

            titre.text = reservation.titre
            dateArrivee.text = "Arrivée : ${reservation.dateArrivee.format(dateFormatter)}"
            dateSortie.text = "Départ : ${reservation.dateSortie.format(dateFormatter)}"
            ville.text = reservation.ville
            pays.text = reservation.pays
            prixTotal.text = "Prix total : ${reservation.prix} €"
            //Glide.with(imageEnTete.context)
                //.load(reservation.imageUrl)
                //.placeholder(R.drawable.image_par_defaut)
               // .error(R.drawable.image_par_defaut)
              //  .into(imageEnTete)

            val daysUntilArrival = ChronoUnit.DAYS.between(LocalDate.now(), reservation.dateArrivee)
            val etatText = when (reservation.status) {
                Reservation.Status.EN_ATTENTE -> "En attente"
                Reservation.Status.ACCEPTÉ -> if (daysUntilArrival >= 0) "Dans $daysUntilArrival jours" else "Séjour en cours"
                Reservation.Status.REFUSÉ -> "Réservation refusée"
                Reservation.Status.COMPLÉTÉ -> "Séjour terminé"
                Reservation.Status.ANNULÉ -> "Réservation annulée"
            }
            etat.text = etatText

            val context = imageEnTete.context
            val imageResId = context.resources.getIdentifier(
                reservation.imageUrl,
                "drawable",
                context.packageName
            )
            imageEnTete.setImageResource(
                if (imageResId != 0) imageResId else R.drawable.image_par_defaut
            )

            itemView.setOnClickListener {
                listener?.onItemClick(adapterPosition)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VueHolderReservation {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.reservation_item, parent, false)
        return VueHolderReservation(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: VueHolderReservation, position: Int) {
        val reservation = presentateur.obtenirRéservation(position)
        holder.bindItems(reservation)
    }

    override fun getItemCount(): Int {
        return presentateur.obtenirNombreRéservations()
    }
}