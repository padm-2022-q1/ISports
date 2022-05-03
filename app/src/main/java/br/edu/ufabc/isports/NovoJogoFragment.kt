package br.edu.ufabc.isports

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.navigation.fragment.findNavController
import br.edu.ufabc.isports.databinding.FragmentNovoJogoBinding
import java.text.SimpleDateFormat
import java.util.*

class NovoJogoFragment : Fragment() {
    private lateinit var binding: FragmentNovoJogoBinding

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
                findNavController().navigate(R.id.action_novoJogoFragment_to_explorarFragment)
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

        val myCalendarDe = Calendar.getInstance()
        val datePickerDe = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMouth ->
            myCalendarDe.set(Calendar.YEAR, year)
            myCalendarDe.set(Calendar.MONTH, month)
            myCalendarDe.set(Calendar.DAY_OF_MONTH, dayOfMouth)
            binding.newGameDataDe.text = sdf.format(myCalendarDe.time)
        }


        binding.newGameDataDe.setOnClickListener{
            DatePickerDialog(it.context, datePickerDe, myCalendarDe.get(Calendar.YEAR), myCalendarDe.get(
                Calendar.MONTH),
                myCalendarDe.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun createSpinner() {
        val spinner: Spinner = binding.tiposJogos
        adapterSpinner(spinner,R.array.tipos_jogos)
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