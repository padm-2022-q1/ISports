package br.edu.ufabc.isports.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import br.edu.ufabc.isports.databinding.FragmentCadastroBinding
import br.edu.ufabc.isports.viewModel.MainViewModel
import com.google.android.material.snackbar.Snackbar


class CadastroFragment : Fragment() {
    private lateinit var binding: FragmentCadastroBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCadastroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        activity?.let {
            bindEvents()
        }
    }

    private fun bindEvents() {
        binding.cadastroButton.setOnClickListener { view ->
            val email = binding.cadastroEmailEditText.text.toString()
            val username = binding.cadastroUsernameEditText.text.toString()
            val password = binding.cadastroPasswordEditText.text.toString()
            val confirmPassword = binding.cadastroConfirmPasswordEditText.text.toString()

            if(email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
                Snackbar.make(view, "Preencha todos os campos", Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(Color.GRAY)
                    .setTextColor(Color.BLACK)
                    .show()
            } else if(password != confirmPassword){
                Snackbar.make(view, "As senhas não batem", Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(Color.GRAY)
                    .setTextColor(Color.BLACK)
                    .show()
            } else{
                viewModel.createUsuario(email, password, username).observe(viewLifecycleOwner) { status ->
                    when(status) {
                        is MainViewModel.Status.Success -> {
                            viewModel.setUsuario().observe(viewLifecycleOwner) {
                                findNavController().navigate(CadastroFragmentDirections.actionCadastroFragmentToMeusJogosFragment(), navOptions {
                                    popUpTo(findNavController().graph.startDestinationId){
                                        inclusive=true
                                    }
                                })
                            }
                        }
                        is MainViewModel.Status.Failure -> {
                            Snackbar.make(view, status.e.message.toString(), Snackbar.LENGTH_SHORT)
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