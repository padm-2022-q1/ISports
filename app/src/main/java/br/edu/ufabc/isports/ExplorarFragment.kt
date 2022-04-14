package br.edu.ufabc.isports

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.navigation.fragment.findNavController
import br.edu.ufabc.isports.databinding.FragmentExplorarBinding

class ExplorarFragment : Fragment() {
    private lateinit var binding: FragmentExplorarBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExplorarBinding.inflate(inflater, container, false)
        bindEvents()
        return binding.root
    }

    private fun bindEvents()
    {
        val spinner: Spinner = binding.tiposJogos
        val dateDe: Spinner = binding.dateDe
        val dateAte: Spinner = binding.dateAte
        adapterSpinner(spinner,R.array.tipos_jogos)
        adapterSpinner(dateDe,R.array.data_range)
        adapterSpinner(dateAte,R.array.data_range)

        binding.bottomNavigationExplorar.setOnItemSelectedListener() { item ->
            when(item.itemId) {
                R.id.menu_meus_jogos -> {
                    findNavController().navigate(R.id.action_explorarFragment_to_meusJogosFragment)
                }
                R.id.menu_perfil -> {
                    findNavController().navigate(R.id.action_explorarFragment_to_perfilFragment)
                }
            }
            true
        }
    }
    private fun adapterSpinner(spinner: Spinner, array:Int){
        ArrayAdapter.createFromResource(
            this.requireContext(),
            array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }
}