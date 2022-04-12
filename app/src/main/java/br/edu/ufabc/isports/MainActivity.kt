package br.edu.ufabc.isports

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.edu.ufabc.isports.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}