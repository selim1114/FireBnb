package com.example.firebnb.présentation.vue

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebnb.R
import com.example.firebnb.présentation.ProprieteDTO
import com.example.firebnb.présentation.présentateur.FavorisPresentateur

class ProprieteFavorisVue : Fragment(), FavorisAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FavorisAdapter
    private var presentateur = FavorisPresentateur(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun getDatabaseHelper(): Context {
        return this.requireContext()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_propriete_favoris_vue, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presentateur.getDatabaseHelper()
        recyclerView = view.findViewById(R.id.recyclerViewFavoris)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = FavorisAdapter(listOf(), this)
        recyclerView.adapter = adapter
        presentateur.accueilDemarrage()
    }


    fun afficherFavoris(proprietes: List<ProprieteDTO>) {
        adapter.updateData(proprietes)
    }

    fun afficherErreur(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
    fun notifierItemSupprimé(id: Int) {
        presentateur.accueilDemarrage()
    }

    override fun onItemClick(id: String) {
        presentateur.supprimerFavoris(id.toInt())
    }
}