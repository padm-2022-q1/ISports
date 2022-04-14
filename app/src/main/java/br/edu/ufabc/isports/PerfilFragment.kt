package br.edu.ufabc.isports

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import br.edu.ufabc.isports.databinding.FragmentPerfilBinding

class PerfilFragment : Fragment() {
    private lateinit var binding: FragmentPerfilBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPerfilBinding.inflate(inflater, container, false)
        binding.bottomNavigationPerfil.selectedItemId = R.id.menu_perfil
        bindEvents()
        return binding.root
    }

    private fun bindEvents() {
        binding.bottomNavigationPerfil.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.menu_meus_jogos -> findNavController().navigate(R.id.action_perfilFragment_to_meusJogosFragment)
                R.id.menu_historico -> findNavController().navigate(PerfilFragmentDirections.actionPerfilFragmentToMeusJogosFragment(isHistorico = true))
                R.id.menu_explorar -> findNavController().navigate(R.id.action_perfilFragment_to_explorarFragment)
            }
            true
        }
    }
}