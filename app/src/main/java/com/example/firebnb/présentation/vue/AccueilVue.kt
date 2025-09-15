package com.example.firebnb.présentation.vue
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebnb.R
import com.example.firebnb.présentation.ProprieteDTO
import com.example.firebnb.présentation.présentateur.AccueilPrésentateur


class AccueilVue : Fragment(), AccueilAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private val présentateur = AccueilPrésentateur(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_accueil_vue, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerViewAccueil)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = AccueilAdapter(listOf(), this)
        initView()
        setupListeners(view)
        val resultats = arguments?.getParcelableArrayList<ProprieteDTO>("resultats")
        présentateur.afficherResultatsOuProprietes(resultats)
    }

    private fun setupListeners(view: View) {
        val clickRoom = view.findViewById<LinearLayout>(R.id.roomAccueilSelect)
        val btnFiltre = view.findViewById<ImageButton>(R.id.filtreBtn)

        clickRoom?.setOnClickListener {
            présentateur.onRoomSelected()
        }

        btnFiltre?.setOnClickListener {
            présentateur.navigation()
        }
    }

    fun initView() {
        présentateur.accueilDemarrage()
    }

    fun afficherListPropriete(valeur: List<ProprieteDTO>) {
        val adapter = AccueilAdapter(valeur, this)
        recyclerView.adapter = adapter
    }

    fun navigationProprieteDetails() {
        findNavController().navigate(R.id.action_accueil_to_propriete)
    }
    fun navigationFilterEcran() {
        findNavController().navigate(R.id.action_accueil_to_filtre)
    }

    fun afficherErreur(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onItemClick(id: String) {
        présentateur.ProprieteSelectionnéeParId(id)
        Log.d("propriété selectionnée","click")

    }
}
