package com.example.firebnb.présentation.vue

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebnb.R
import com.example.firebnb.domaine.entité.Reservation
import com.example.firebnb.présentation.modèle.ModèleImpl
import com.example.firebnb.présentation.présentateur.RéservationPrésentateur
import com.example.firebnb.présentation.présentateur.RéservationPrésentateurImpl

class ReservationsHistoriqueVue : Fragment(), ReservationVue {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReservationAdapter
    private lateinit var presentateur: RéservationPrésentateur

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presentateur = RéservationPrésentateurImpl( this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reservations_en_cours_vue, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewEnCours)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ReservationAdapter(presentateur)
        adapter.setOnItemClickListener(object : ReservationAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                presentateur.gérerClicRéservation(position)
            }
        })
        recyclerView.adapter = adapter

        presentateur.chargerHistoriqueRéservations()
    }

    override fun afficherRéservations(reservations: List<Reservation>) {
        adapter.notifyDataSetChanged()
    }

    override fun notifierItemInséré(position: Int) {
        adapter.notifyItemInserted(position)
        recyclerView.scrollToPosition(position)
    }

    override fun notifierItemSupprimé(position: Int) {
        adapter.notifyItemRemoved(position)
    }

    override fun afficherErreur(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun naviguerVersDétails(bundle: Bundle) {
        Navigation.findNavController(requireView()).navigate(
            R.id.action_historique_to_detailReservation,
            bundle
        )
    }
}