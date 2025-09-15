package com.example.firebnb.présentation.vue

import android.annotation.SuppressLint
import android.icu.util.DateInterval
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.firebnb.R
import java.text.NumberFormat
import com.example.firebnb.présentation.ProprieteDTO
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AccueilAdapter(
    var valeur: List<ProprieteDTO>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<AccueilAdapter.VueHolderPropriete>() {
    interface OnItemClickListener {
        fun onItemClick(id: String)
    }

    inner class VueHolderPropriete(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var proprietePhoto: ImageView = itemView.findViewById(R.id.proprietePhoto)
        private val proprieteTitre: TextView = itemView.findViewById(R.id.proprieteTitre)
        private val proprieteDescription: TextView =
            itemView.findViewById(R.id.proprieteDescription)
        private val proprietePrix: TextView = itemView.findViewById(R.id.proprietePrix)
        private val proprieteDatesDisponibles: TextView =
            itemView.findViewById(R.id.prochaineDisponibiliteDate)

        @SuppressLint("StringFormatMatches")
        fun bindItems(model: ProprieteDTO) {
            proprieteTitre.text = model.titre
            proprieteDescription.text = model.description
            proprietePrix.text = getFormattedCurrency(model.prix.toDouble())
            proprieteDatesDisponibles.text = itemView.context.getString(
                R.string.prochaineDisponibiliteDate,
                convertClosestDate(model.dateDisponible, "from"),
                convertClosestDate(model.dateDisponible, "to")
            )
            val context = proprietePhoto.context
            val imageResId = context.resources.getIdentifier(
                model.photo,
                "drawable",
                context.packageName
            )
            proprietePhoto.setImageResource(
                if (imageResId != 0) imageResId else R.drawable.image_par_defaut
            )

            itemView.setOnClickListener {
                listener?.onItemClick(model.id)
            }
        }
    }
    //val context = proprietePhoto.context
    // Glide.with(context)
    //    .load(model.photo)
    //   .apply(RequestOptions().placeholder(R.drawable.image_par_defaut))
    //  .into(proprietePhoto)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VueHolderPropriete {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.accueil_item, parent, false)
        return VueHolderPropriete(view)
    }

    override fun onBindViewHolder(holder: VueHolderPropriete, position: Int) {
        holder.bindItems(valeur[position])
    }

    override fun getItemCount(): Int {
       return valeur.size
    }


    //Convertir Stringto en DateListe puis en un string qui est closest date
    fun convertClosestDate(dateDisponible: String, type: String): String {
        Log.d("dateDisponible", "dateDisponible $dateDisponible")
        val dispo = ArrayList<DateInterval>()
        if(dateDisponible.contains(",")) {
            dateDisponible.split(",").map { uneDispo ->
                val journee = uneDispo.split(" to ")
                val dateFormat = SimpleDateFormat("dd MMM yyyy")
                dispo.add(DateInterval(dateFormat.parse(journee[0]).time, dateFormat.parse(journee[1]).time))
            }
            return getClosestDate(dispo,type)
        }
        else if (dateDisponible.contains("to")){
            var journee = dateDisponible.split(" to ")
             val datePicked = when (type) {
                "from" -> journee[0]
                "to" -> journee[1]
                else -> ""
            }
            return datePicked
        }
        return "_"
    }

    //convertir closest
    fun getClosestDate(dateDisponible :List<DateInterval>,type: String): String {
        val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val closestDateInterval = dateDisponible.minByOrNull { it.fromDate }
        val datePicked = when (type) {
            "from" -> closestDateInterval?.fromDate
            "to" -> closestDateInterval?.toDate
            else -> closestDateInterval?.fromDate
        }
        return if (datePicked != null) {
            formatter.format(Date(datePicked))
        } else ""
    }

    private fun getFormattedCurrency( price: Double): String {
        val locale = Locale.getDefault()
        val currencyFormatter = NumberFormat.getCurrencyInstance(locale)
        return currencyFormatter.format(price)
    }

}

