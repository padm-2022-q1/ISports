package br.edu.ufabc.isports.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import br.edu.ufabc.isports.R
import br.edu.ufabc.isports.databinding.FragmentRecuperarSenhaBinding
import br.edu.ufabc.isports.viewModel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class RecuperarSenhaFragment : Fragment(){
    private lateinit var  binding: FragmentRecuperarSenhaBinding
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecuperarSenhaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = ProgressBar(binding.progressHorizontal)
    }

    override fun onStart() {
        super.onStart()
        activity?.let {
            bindEvents()
        }
    }

    private fun bindEvents() {
        binding.redefinirSenhaButton.setOnClickListener { view ->
            val email = binding.recuperarSenhaEmailEditText.text.toString()

            if(email.isEmpty()){
                Snackbar.make(view, "Preencha todos os campos", Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(Color.GRAY)
                    .setTextColor(Color.BLACK)
                    .show()
            } else{
                viewModel.recuperarSenha(email).observe(viewLifecycleOwner) { status ->
                    when (status) {
                        is MainViewModel.Status.Loading -> progressBar.start()
                        is MainViewModel.Status.Success -> {
                            Snackbar.make( view, "Verifique sua caixa de email", Snackbar.LENGTH_SHORT)
                                .setBackgroundTint(Color.GRAY)
                                .setTextColor(Color.BLACK)
                                .show()
                            findNavController().navigate(R.id.action_recuperarSenhaFragment_to_loginFragment)
                        }
                        is MainViewModel.Status.Failure -> {
                            progressBar.stop()
                            Snackbar.make(view, status.e.message.toString(), Snackbar.LENGTH_SHORT)
                                .setBackgroundTint(Color.GRAY)
                                .setTextColor(Color.BLACK)
                                .show()
                        }
                    }
                }
            }
        }
    }
}