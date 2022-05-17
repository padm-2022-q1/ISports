package br.edu.ufabc.isports.view

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.isports.R
import br.edu.ufabc.isports.databinding.FragmentJogosDetailsBinding
import br.edu.ufabc.isports.databinding.ListParticipantesBinding
import br.edu.ufabc.isports.viewModel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class JogosDetailsFragment : Fragment() {

    private lateinit var binding: FragmentJogosDetailsBinding
    private val args: JogosDetailsFragmentArgs by navArgs()
    private val viewModel: MainViewModel by activityViewModels()
    companion object{
        lateinit var participantes_username: List<String>
    }

    private inner class ContactAdapter(val contacts: List<String>) :
        RecyclerView.Adapter<ContactAdapter.ContactHolder>() {


        private inner class ContactHolder(itemBinding: ListParticipantesBinding) :
            RecyclerView.ViewHolder(itemBinding.root) {

            val participante = itemBinding.participante
            }


        /**
         * Create a view holder.
         */
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ContactHolder =
            ContactHolder(
                ListParticipantesBinding.inflate(
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

            holder.participante.text = contact
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
        binding = FragmentJogosDetailsBinding.inflate(inflater, container, false)
        initTexts()
        bindEvents()
        return binding.root
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

    private fun initTexts() {

        args.jogoItem.takeIf { it?.id?.isNotEmpty() == true }?.also { jogoItem ->
            binding.modalidade.text = jogoItem.modalidade
            binding.inicio.text = jogoItem.inicio.toString()
            binding.fim.text = jogoItem.fim.toString()
            binding.local.text = jogoItem.local
            binding.recyclerviewParticipantes.apply {
            }
        }
    }

}