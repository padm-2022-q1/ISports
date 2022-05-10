package br.edu.ufabc.isports.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import br.edu.ufabc.isports.R
import br.edu.ufabc.isports.databinding.FragmentMeusJogosBinding

class HistoricoFragment : Fragment() {
    private lateinit var binding: FragmentMeusJogosBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMeusJogosBinding.inflate(inflater, container, false)
        binding.meusJogosMatchNotFoundTextView.text = getString(R.string.match_not_found_historico)
        binding.fabHistoric.text = getString(R.string.fab_historic_text_historico)
        bindEvents()
        return binding.root
    }
    private fun bindEvents() {
        binding.bottomNavigationJogos.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.menu_explorar -> findNavController().navigate(R.id.action_historicoFragment_to_explorarFragment)
                R.id.menu_perfil -> findNavController().navigate(R.id.action_historicoFragment_to_perfilFragment)
            }
            true
        }
        binding.fabHistoric.setOnClickListener {
            findNavController().navigate(R.id.action_historicoFragment_to_meusJogosFragment)
        }
    }

}