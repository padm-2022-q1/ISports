package br.edu.ufabc.isports.view

import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import br.edu.ufabc.isports.viewModel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class JogosDetailsMeusJogosFragment : JogosDetailsFragment() {
    override fun removePlayer() {
        binding.fabParticipar.visibility = View.INVISIBLE
        binding.fabSair.visibility = View.VISIBLE
        binding.fabSair.setOnClickListener{view ->
            args.jogoItem?.id?.let{ id ->
                viewModel.removeParticipante(id).observe(viewLifecycleOwner){ status ->
                    when(status){
                        is MainViewModel.Status.Success -> {
                            Snackbar.make(view, "Removido com sucesso", Snackbar.LENGTH_SHORT)
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
}