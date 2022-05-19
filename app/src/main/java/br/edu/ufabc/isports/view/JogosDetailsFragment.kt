package br.edu.ufabc.isports.view

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.isports.R
import br.edu.ufabc.isports.databinding.FragmentJogosDetailsBinding
import br.edu.ufabc.isports.databinding.ListParticipantesBinding
import br.edu.ufabc.isports.model.objects.Participante
import br.edu.ufabc.isports.viewModel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

open class JogosDetailsFragment : Fragment() {

    lateinit var binding: FragmentJogosDetailsBinding
    val args: JogosDetailsFragmentArgs by navArgs()
    val viewModel: MainViewModel by activityViewModels()

    private inner class ParticipantesAdapater(val participantes: List<Participante>) :
        RecyclerView.Adapter<ParticipantesAdapater.ParticipantesHolder>() {


        private inner class ParticipantesHolder(itemBinding: ListParticipantesBinding) :
            RecyclerView.ViewHolder(itemBinding.root) {
            val participante = itemBinding.participante
            }


        /**
         * Create a view holder.
         */
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ParticipantesHolder =
            ParticipantesHolder(
                ListParticipantesBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        /**
         * Populate a view holder with data.
         */
        override fun onBindViewHolder(holder: ParticipantesHolder, position: Int) {
            val participant = participantes[position]
            holder.participante.text = participant.username
        }

        /**
         * The total quantity of items in the list.
         */
        override fun getItemCount(): Int = participantes.size

        /**
         * Called when a view holder is recycled.
         */
        override fun onViewRecycled(holder: ParticipantesHolder) {
            super.onViewRecycled(holder)
            Log.d("APP", "Recycled holder at position ${holder.bindingAdapterPosition}")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJogosDetailsBinding.inflate(inflater, container, false)
        initTexts()
        bindEvents()
        removePlayer()
        unshow()
        return binding.root
    }

    open fun removePlayer(){

    }

    open fun unshow(){

    }

    private fun bindEvents() {
        binding.fabParticipar.setOnClickListener { view ->
            args.jogoItem?.id?.let{ id ->
                viewModel.addParticipante(id).observe(viewLifecycleOwner){ status ->
                    when(status){
                        is MainViewModel.Status.Success -> {
                            Snackbar.make(view, "Participando com sucesso", Snackbar.LENGTH_SHORT)
                                .setBackgroundTint(Color.GRAY)
                                .setTextColor(Color.BLACK)
                                .show()
                            viewModel.clickedItemId.value=null
                            findNavController().navigateUp()
                        }
                        is MainViewModel.Status.Failure -> {
                            Snackbar.make(binding.root, status.e.message.toString(), Snackbar.LENGTH_SHORT)
                                .setBackgroundTint(Color.GRAY)
                                .setTextColor(Color.BLACK)
                                .show()
                        }
                        else -> { }
                    }
                }
            }
        }
    }

    private fun setIcon(icon:String):Int
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

    private fun initTexts() {
        val sdf = SimpleDateFormat("dd/MM/yyyy H:mm", Locale.getDefault())
        args.jogoItem.takeIf { it?.id?.isNotEmpty() == true }?.also { jogoItem ->
            binding.modalidade.text = jogoItem.modalidade
            binding.inicio.text = StringBuilder().append("Inicio: ").append(sdf.format(jogoItem.inicio))
            binding.fim.text = StringBuilder().append("Fim: ").append(sdf.format(jogoItem.fim))
            binding.local.text = StringBuilder().append("Local: ").append(jogoItem.local)
            binding.recyclerviewParticipantes.apply {
                adapter = ParticipantesAdapater(jogoItem.participantes)
            }
            binding.notesListFavorites.setImageResource(setIcon(jogoItem.modalidade))
        }
    }

}