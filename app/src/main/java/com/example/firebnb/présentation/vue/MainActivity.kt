package com.example.firebnb.présentation.vue

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.firebnb.R
import com.example.firebnb.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as? NavHostFragment
        val navController = navHostFragment?.navController

        navController?.let { binding.bottomBar.setupWithNavController(it) }

        binding.bottomBar.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.fragment_accueil -> {
                    navController?.navigate(R.id.accueil)
                    true
                }

                R.id.fragment_historique -> {
                    navController?.navigate(R.id.historique)
                    true
                }

                R.id.fragment_profil -> {
                    navController?.navigate(R.id.fragment_profil)
                    true
                }
                R.id.favoris -> {
                    navController?.navigate(R.id.favoris)
                    true
                }

                else -> false
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}

