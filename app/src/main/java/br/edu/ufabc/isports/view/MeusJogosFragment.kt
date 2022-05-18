package br.edu.ufabc.isports.view

import android.graphics.Color
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
import br.edu.ufabc.isports.model.objects.Jogo
import br.edu.ufabc.isports.viewModel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class MeusJogosFragment : Fragment() {
    private lateinit var binding: FragmentMeusJogosBinding
    private val viewModel: MainViewModel by activityViewModels()

    private inner class JogosAdapter(val jogos: List<Jogo>) :
        RecyclerView.Adapter<JogosAdapter.JogosHolder>() {


        private inner class JogosHolder(itemBinding: JogosListItemBinding) :
            RecyclerView.ViewHolder(itemBinding.root) {

            val modalidade = itemBinding.jogosListModalidade
            val dateCreated = itemBinding.jogosListDataDia
            val time = itemBinding.jogosListTime
            val endereco = itemBinding.jogosListEndereco
            val icon = itemBinding.notesListFavorites
            init {
                itemBinding.root.setOnClickListener {
                    viewModel.clickedItemId.value = jogos[bindingAdapterPosition]
                }

            }
        }

        /**
         * Create a view holder.
         */
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): JogosHolder =
            JogosHolder(
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
        override fun onBindViewHolder(holder: JogosHolder, position: Int) {
            val contact = jogos[position]

            holder.modalidade.text = contact.modalidade
            holder.dateCreated.text = contact.inicio.toString()
            //holder.time.text = contact.timeStart
            holder.endereco.text = contact.local
            holder.icon.setImageResource(setIcon(contact.modalidade))
        }

        /**
         * The total quantity of items in the list.
         */
        override fun getItemCount(): Int = jogos.size

        /**
         * Called when a view holder is recycled.
         */
        override fun onViewRecycled(holder: JogosHolder) {
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
        bindEvents()
        return binding.root
    }

    private fun reset(){
        binding.swipeRefreshLayout.isRefreshing = true
        binding.recyclerviewMeusJogos.apply {
            adapter = JogosAdapter(emptyList())
        }
        viewModel.getMeusJogos().observe(viewLifecycleOwner) { status ->
            when(status) {
                is MainViewModel.Status.Success -> {
                    (status.result as MainViewModel.Result.GetJogos).value.let{
                        if(it.isEmpty()){
                            binding.meusJogosMatchNotFoundTextView.visibility = View.VISIBLE
                        } else{
                            binding.recyclerviewMeusJogos.apply {
                                viewModel.usuario.meusJogos = it
                                adapter = JogosAdapter(it)
                            }
                        }
                    }
                }
                is MainViewModel.Status.Failure -> {
                    Snackbar.make(binding.root, status.e.message.toString(), Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(Color.GRAY)
                        .setTextColor(Color.BLACK)
                        .show()
                }
                else -> {}
            }
        }
        binding.swipeRefreshLayout.isRefreshing = false
    }

    override fun onStart() {
        super.onStart()
        binding.recyclerviewMeusJogos.apply {
            adapter = JogosAdapter(viewModel.usuario.meusJogos)
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            reset()
        }
    }
    private fun bindEvents() {
        viewModel.clickedItemId.observe(viewLifecycleOwner){
            if(viewModel.clickedItemId.value!=null){
                findNavController().navigate(MeusJogosFragmentDirections.onClickItemMeusJogos(it), navOptions {
                    popUpTo(R.id.meusJogosFragment)
                })
            }
        }
        binding.bottomNavigationJogos.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.menu_explorar -> findNavController().navigate(R.id.action_meusJogosFragment_to_explorarFragment)
                R.id.menu_perfil -> findNavController().navigate(R.id.action_meusJogosFragment_to_perfilFragment)
            }
            true
        }
        binding.fabHistoric.setOnClickListener {
            findNavController().navigate(R.id.action_meusJogosFragment_to_historicoFragment)
        }
    }



}