package br.edu.ufabc.isports

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.navigation.fragment.findNavController
import br.edu.ufabc.isports.databinding.FragmentMeusJogosBinding

class MeusJogosFragment : Fragment() {
    private lateinit var binding: FragmentMeusJogosBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMeusJogosBinding.inflate(inflater, container, false)
        bindEvents()
        return binding.root
    }
    private fun bindEvents() {
        binding.bottomNavigationJogos.setOnItemSelectedListener() { item ->
            when(item.itemId) {
                R.id.menu_explorar -> {
                    findNavController().navigate(R.id.action_meusJogosFragment_to_explorarFragment)
                }
                R.id.menu_perfil -> {
                    findNavController().navigate(R.id.action_meusJogosFragment_to_perfilFragment)
                }
            }
            true
        }
    }

}