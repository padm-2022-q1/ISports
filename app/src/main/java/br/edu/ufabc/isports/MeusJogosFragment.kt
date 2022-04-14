package br.edu.ufabc.isports

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.edu.ufabc.isports.databinding.FragmentMeusJogosBinding

class MeusJogosFragment : Fragment() {
    private lateinit var binding: FragmentMeusJogosBinding
    private  val _args : MeusJogosFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMeusJogosBinding.inflate(inflater, container, false)

        if(_args.isHistorico) {
            binding.meusJogosMatchNotFoundTextView.text = getString(R.string.match_not_found_historico)
            binding.bottomNavigationJogos.selectedItemId = R.id.menu_historico
        }
        bindEvents()
        return binding.root
    }
    private fun bindEvents() {
        binding.bottomNavigationJogos.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.menu_meus_jogos -> if(_args.isHistorico) findNavController().navigate(MeusJogosFragmentDirections.actionMeusJogosFragmentSelf(isHistorico = false))
                R.id.menu_historico -> if(!_args.isHistorico) findNavController().navigate(MeusJogosFragmentDirections.actionMeusJogosFragmentSelf(isHistorico = true))
                R.id.menu_explorar -> findNavController().navigate(R.id.action_meusJogosFragment_to_explorarFragment)
                R.id.menu_perfil -> findNavController().navigate(R.id.action_meusJogosFragment_to_perfilFragment)
            }
            true
        }
    }

}