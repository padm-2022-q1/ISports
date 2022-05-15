package br.edu.ufabc.isports.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.isports.viewModel.MainViewModel
import br.edu.ufabc.isports.R
import br.edu.ufabc.isports.databinding.FragmentExplorarBinding
import br.edu.ufabc.isports.databinding.JogosListItemBinding
import br.edu.ufabc.isports.model.Jogo
import java.text.SimpleDateFormat
import java.util.*

class ExplorarFragment : Fragment() {
    private lateinit var binding: FragmentExplorarBinding
    private val viewModel: MainViewModel by activityViewModels()

    companion object{
        lateinit var modalidadesJogos: List<String>
    }
    private inner class ContactAdapter(val contacts: List<Jogo>) :
        RecyclerView.Adapter<ContactAdapter.ContactHolder>() {


        private inner class ContactHolder(itemBinding: JogosListItemBinding) :
            RecyclerView.ViewHolder(itemBinding.root) {

            val modalidade = itemBinding.jogosListModalidade
            val dateCreated = itemBinding.jogosListDataDia
            val time = itemBinding.jogosListTime
            val endereco = itemBinding.jogosListEndereco
            val icon = itemBinding.notesListFavorites
            init {
                itemBinding.root.setOnClickListener {
                    viewModel.clickedItemId.value = contacts[bindingAdapterPosition]
                }

            }
        }

        /**
         * Create a view holder.
         */
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ContactHolder =
            ContactHolder(
                JogosListItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        fun setIcon(icon:String):Int
        {
            return when (icon) {
                "Basquete" -> {
                    R.drawable.ic_baseline_sports_basketball
                }
                "Tenis" -> {
                    R.drawable.ic_baseline_sports_tennis
                }
                "Vôlei" -> {
                    R.drawable.ic_baseline_sports_volleyball
                }
                else -> R.drawable.ic_baseline_sports_soccer
            }
        }
        /**
         * Populate a view holder with data.
         */
        override fun onBindViewHolder(holder: ContactHolder, position: Int) {
            val contact = contacts[position]

            holder.modalidade.text = contact.modalidade
            holder.dateCreated.text = contact.inicio.toString()
            //holder.time.text = contact.timeStart
            holder.endereco.text = contact.local
            holder.icon.setImageResource(setIcon(contact.modalidade))
        }

        /**
         * The total quantity of items in the list.
         */
        override fun getItemCount(): Int = contacts.size

        /**
         * Called when a view holder is recycled.
         */
        override fun onViewRecycled(holder: ContactHolder) {
            super.onViewRecycled(holder)
            Log.d("APP", "Recycled holder at position ${holder.bindingAdapterPosition}")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExplorarBinding.inflate(inflater, container, false)
        modalidadesJogos = resources.getStringArray(R.array.tipos_jogos).toList()
        bindEvents()
        createSpinner()
        createDate()
        createTime()
        return binding.root
    }

    private fun createTime() {
        val timeDialogDe = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            binding.explorarTimeDe.text = String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
        }
        val timeDialogAte = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            binding.explorarTimeAte.text = String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
        }
        binding.explorarTimeDe.setOnClickListener {
            TimePickerDialog(it.context, timeDialogDe, 0, 0, true).show()
        }
        binding.explorarTimeAte.setOnClickListener {
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
        adapterSpinner(spinner, R.array.tipos_jogos)
    }

    private fun bindEvents()
    {
        viewModel.clickedItemId.observe(viewLifecycleOwner){
            if(viewModel.clickedItemId.value!=null){
                findNavController().navigate(ExplorarFragmentDirections.onClickItem(it), navOptions {
                    popUpTo(R.id.explorarFragment)
                })
            }
        }
        binding.bottomNavigationExplorar.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.menu_meus_jogos -> findNavController().navigate(R.id.action_explorarFragment_to_meusJogosFragment)
                R.id.menu_perfil -> findNavController().navigate(R.id.action_explorarFragment_to_perfilFragment)
            }
            true
        }
        binding.explorarFiltrarButton.setOnClickListener {
            viewModel.getJogosExplorar(binding.tiposJogos.selectedItem.toString()).observe(viewLifecycleOwner) { status ->
                when(status) {
                    is MainViewModel.Status.Success -> {
                        (status.result as MainViewModel.Result.GetJogos).value.let{
                            binding.recyclerviewJogos.apply {
                                adapter = ContactAdapter(it)
                            }
                        }
                    }
                    else -> {}
                }
            }
        }

        binding.fabAddJogo.setOnClickListener {
            findNavController().navigate(R.id.action_explorarFragment_to_novoJogoFragment)
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