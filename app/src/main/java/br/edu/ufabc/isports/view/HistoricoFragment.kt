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
import java.lang.StringBuilder

class HistoricoFragment : Fragment() {
    private lateinit var binding: FragmentMeusJogosBinding
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var progressBar: ProgressBar
    private inner class HistoricoAdapter(val historico: List<Jogo>) :
        RecyclerView.Adapter<HistoricoAdapter.HistoricoHolder>() {


        private inner class HistoricoHolder(itemBinding: JogosListItemBinding) :
            RecyclerView.ViewHolder(itemBinding.root) {

            val modalidade = itemBinding.jogosListModalidade
            val inicio = itemBinding.inicio
            val fim = itemBinding.fim
            val local = itemBinding.local
            val icon = itemBinding.notesListFavorites
            init {
                itemBinding.root.setOnClickListener {
                    viewModel.clickedItemId.value = historico[bindingAdapterPosition]
                }

            }
        }

        /**
         * Create a view holder.
         */
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): HistoricoHolder =
            HistoricoHolder(
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
        override fun onBindViewHolder(holder: HistoricoHolder, position: Int) {
            val historic = historico[position]

            holder.modalidade.text = historic.modalidade
            holder.inicio.text = StringBuilder().append("Inicio: ").append(historic.inicio.toString())
            holder.fim.text = StringBuilder().append("Fim: ").append(historic.fim.toString())
            holder.local.text = StringBuilder().append("Local: ").append(historic.local)
            holder.icon.setImageResource(setIcon(historic.modalidade))
        }

        /**
         * The total quantity of items in the list.
         */
        override fun getItemCount(): Int = historico.size

        /**
         * Called when a view holder is recycled.
         */
        override fun onViewRecycled(holder: HistoricoHolder) {
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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = ProgressBar(binding.progressHorizontal)
    }
    private fun bindEvents() {
        viewModel.clickedItemId.observe(viewLifecycleOwner){
            if(viewModel.clickedItemId.value!=null){
                findNavController().navigate(HistoricoFragmentDirections.detailsHistoricoItem(it), navOptions {
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
        binding.recyclerviewMeusJogos.apply {
            adapter = HistoricoAdapter(emptyList())
        }
        viewModel.getHistorico().observe(viewLifecycleOwner) { status ->
            when(status) {
                is MainViewModel.Status.Success -> {
                    (status.result as MainViewModel.Result.GetJogos).value.let{
                        if(it.isEmpty()){
                            binding.meusJogosMatchNotFoundTextView.visibility = View.VISIBLE
                        } else{
                            binding.recyclerviewMeusJogos.apply {
                                viewModel.usuario.historico = it
                                adapter = HistoricoAdapter(it)
                            }
                        }
                    }
                    progressBar.stop()
                }
                is MainViewModel.Status.Failure -> {
                    Snackbar.make(binding.root, status.e.message.toString(), Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(Color.GRAY)
                        .setTextColor(Color.BLACK)
                        .show()
                    progressBar.stop()
                }
                is MainViewModel.Status.Loading -> {
                    progressBar.start()
                }
                else -> {}
            }
        }
        binding.swipeRefreshLayout.isRefreshing = false
    }
    override fun onStart() {
        super.onStart()
        binding.recyclerviewMeusJogos.apply {
            adapter = HistoricoAdapter(viewModel.usuario.historico)
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            reset()
        }
    }

}