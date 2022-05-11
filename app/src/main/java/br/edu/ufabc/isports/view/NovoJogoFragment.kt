package br.edu.ufabc.isports.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import br.edu.ufabc.isports.R
import br.edu.ufabc.isports.databinding.FragmentNovoJogoBinding
import br.edu.ufabc.isports.model.JogoFirestore
import br.edu.ufabc.isports.model.Participantes
import br.edu.ufabc.isports.viewModel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

class NovoJogoFragment : Fragment() {
    private lateinit var binding: FragmentNovoJogoBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNovoJogoBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        createSpinner()
        createDate()
        createTime()
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                val list = mutableListOf<Participantes>()
                list.add(Participantes(viewModel.usuario.id, viewModel.usuario.username))
                JogoFirestore(
                    modalidade = binding.tiposJogos.selectedItem.toString(),
                    inicio = sdf.parse("${binding.newGameData.text} ${binding.newGameTimeDe.text}")!!,
                    fim = sdf.parse("${binding.newGameData.text} ${binding.newGameTimeAte.text}")!!,
                    local = binding.cadastroEndereco.text.toString(),
                    participantes = list
                ).let { jogo ->
                    viewModel.addJogo(jogo).observe(viewLifecycleOwner) { status ->
                        when(status) {
                            is MainViewModel.Status.Success -> {
                                findNavController().navigate(R.id.action_novoJogoFragment_to_explorarFragment)
                            }
                            else -> { }
                        }
                    }
                }
            }
        }
        return true
    }

    private fun createTime() {
        val timeDialogDe = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            binding.newGameTimeDe.text= String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
        }

        val timeDialogAte = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            binding.newGameTimeAte.text= String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
        }
        binding.newGameTimeDe.setOnClickListener {
            TimePickerDialog(it.context, timeDialogDe, 0, 0, true).show()
        }
        binding.newGameTimeAte.setOnClickListener {
            TimePickerDialog(it.context, timeDialogAte, 0, 0, true).show()
        }
    }

    private fun createDate() {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        val myCalendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMouth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMouth)
            binding.newGameData.text = sdf.format(myCalendar.time)
        }

        binding.newGameData.setOnClickListener{
            DatePickerDialog(it.context, datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(
                Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun createSpinner() {
        val spinner: Spinner = binding.tiposJogos
        adapterSpinner(spinner, R.array.tipos_jogos)
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