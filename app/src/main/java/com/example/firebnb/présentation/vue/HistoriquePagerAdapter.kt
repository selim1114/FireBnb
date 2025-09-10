package com.example.firebnb.présentation.vue

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class HistoriquePagerAdapter(val fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ReservationsEnCoursVue()
            1 -> ReservationsHistoriqueVue()
            else -> throw IllegalStateException("Position invalide")
        }
    }
}