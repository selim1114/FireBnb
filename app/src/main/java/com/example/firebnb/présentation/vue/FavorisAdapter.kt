package com.example.firebnb.présentation.vue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebnb.R
import com.example.firebnb.présentation.ProprieteDTO

class FavorisAdapter(
    private var valeur: List<ProprieteDTO>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<FavorisAdapter.VueHolderPropriete>() {

    interface OnItemClickListener {
        fun onItemClick(id: String)
    }

    inner class VueHolderPropriete(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val headerImage: ImageView = itemView.findViewById(R.id.header_image)
        private val titre: TextView = itemView.findViewById(R.id.titre)
        private val prixTotal: TextView = itemView.findViewById(R.id.prix_total)
        private val ville: TextView = itemView.findViewById(R.id.ville)
        private val pays: TextView = itemView.findViewById(R.id.pays)
        private val likeButton: ImageButton = itemView.findViewById(R.id.imageButton)

        fun bindItems(model: ProprieteDTO) {
            titre.text = model.titre
            prixTotal.text = "Prix : ${model.prix} $"
            ville.text = model.adresse.split(",").firstOrNull()
            pays.text = model.adresse.split(",").lastOrNull()

            Glide.with(headerImage.context)
                .load(model.photo)
                .placeholder(R.drawable.image_par_defaut)
                .error(R.drawable.image_par_defaut)
                .into(headerImage)

            likeButton.setOnClickListener {
                listener.onItemClick(model.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VueHolderPropriete {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.favoris_item, parent, false)
        return VueHolderPropriete(view)
    }

    override fun onBindViewHolder(holder: VueHolderPropriete, position: Int) {
        holder.bindItems(valeur[position])
    }

    override fun getItemCount(): Int = valeur.size

    fun updateData(newValeur: List<ProprieteDTO>) {
        valeur = newValeur
        notifyDataSetChanged()
    }
}