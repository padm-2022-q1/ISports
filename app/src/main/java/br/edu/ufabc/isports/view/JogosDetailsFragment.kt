package br.edu.ufabc.isports.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import br.edu.ufabc.isports.databinding.FragmentJogosDetailsBinding
import br.edu.ufabc.isports.viewModel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class JogosDetailsFragment : Fragment() {

    private lateinit var binding: FragmentJogosDetailsBinding
    private val args: JogosDetailsFragmentArgs by navArgs()
    private val viewModel: MainViewModel by activityViewModels()

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
                        else -> { }
                    }
                }
            }
        }
    }

    private fun initTexts() {
        args.jogoItem.takeIf { it?.id?.isNotEmpty() == true }?.also { jogoItem ->
            binding.jogoId.text = jogoItem.id
        }
    }

}