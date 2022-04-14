package br.edu.ufabc.isports

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.navigation.fragment.findNavController
import br.edu.ufabc.isports.databinding.FragmentExplorarBinding
import java.text.SimpleDateFormat
import java.util.*

class ExplorarFragment : Fragment() {
    private lateinit var binding: FragmentExplorarBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExplorarBinding.inflate(inflater, container, false)
        bindEvents()
        createSpinner()
        createDate()
        return binding.root
    }

    private fun createDate() {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        val myCalendarDe = Calendar.getInstance()
        val datePickerDe = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMouth ->
            myCalendarDe.set(Calendar.YEAR, year)
            myCalendarDe.set(Calendar.MONTH, month)
            myCalendarDe.set(Calendar.DAY_OF_MONTH, dayOfMouth)
            binding.explorarDataDe.text = sdf.format(myCalendarDe.time)
        }

        val myCalendarAte = Calendar.getInstance()
        val datePickerAte = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMouth ->
            myCalendarAte.set(Calendar.YEAR, year)
            myCalendarAte.set(Calendar.MONTH, month)
            myCalendarAte.set(Calendar.DAY_OF_MONTH, dayOfMouth)
            binding.explorarDataAte.text = sdf.format(myCalendarAte.time)
        }

        binding.explorarDataDe.setOnClickListener{
            DatePickerDialog(it.context, datePickerDe, myCalendarDe.get(Calendar.YEAR), myCalendarDe.get(Calendar.MONTH),
                myCalendarDe.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.explorarDataAte.setOnClickListener{
            DatePickerDialog(it.context, datePickerAte, myCalendarAte.get(Calendar.YEAR), myCalendarAte.get(Calendar.MONTH),
                myCalendarAte.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun createSpinner() {
        val spinner: Spinner = binding.tiposJogos
        adapterSpinner(spinner,R.array.tipos_jogos)
    }

    private fun bindEvents()
    {
        binding.bottomNavigationExplorar.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.menu_meus_jogos -> findNavController().navigate(R.id.action_explorarFragment_to_meusJogosFragment)
                R.id.menu_historico -> findNavController().navigate(ExplorarFragmentDirections.actionExplorarFragmentToMeusJogosFragment(isHistorico = true))
                R.id.menu_perfil -> findNavController().navigate(R.id.action_explorarFragment_to_perfilFragment)
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