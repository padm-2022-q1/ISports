package br.edu.ufabc.isports.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.isports.R
import br.edu.ufabc.isports.databinding.FragmentMeusJogosBinding
import br.edu.ufabc.isports.databinding.JogosListItemBinding
import br.edu.ufabc.isports.model.Jogo
import br.edu.ufabc.isports.viewModel.MainViewModel

class HistoricoFragment : Fragment() {
    private lateinit var binding: FragmentMeusJogosBinding
    private val viewModel: MainViewModel by activityViewModels()
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
        binding = FragmentMeusJogosBinding.inflate(inflater, container, false)
        binding.meusJogosMatchNotFoundTextView.text = getString(R.string.match_not_found_historico)
        binding.fabHistoric.text = getString(R.string.fab_historic_text_historico)
        bindEvents()
        return binding.root
    }
    private fun bindEvents() {
        viewModel.clickedItemId.observe(viewLifecycleOwner){
            if(viewModel.clickedItemId.value!=null){
                findNavController().navigate(HistoricoFragmentDirections.onClickItemHistorico(it), navOptions {
                    popUpTo(R.id.historicoFragment)
                })
            }
        }
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

    private fun reset(){
        binding.swipeRefreshLayout.isRefreshing = true
        viewModel.getHistorico().observe(viewLifecycleOwner) { status ->
            when(status) {
                is MainViewModel.Status.Success -> {
                    (status.result as MainViewModel.Result.GetJogos).value.let{
                        if(it.isEmpty()){
                            binding.meusJogosMatchNotFoundTextView.visibility = View.VISIBLE
                        } else{
                            binding.recyclerviewMeusJogos.apply {
                                adapter = ContactAdapter(it)
                            }
                        }
                    }
                }
                else -> {}
            }
        }
        binding.swipeRefreshLayout.isRefreshing = false
    }
    override fun onStart() {
        super.onStart()
        reset()
        binding.swipeRefreshLayout.setOnRefreshListener {
            reset()
        }
    }

}