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
import br.edu.ufabc.isports.R
import br.edu.ufabc.isports.databinding.FragmentLoginBinding
import br.edu.ufabc.isports.viewModel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.*

class LoginFragment : Fragment(){
    private lateinit var  binding: FragmentLoginBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        activity?.let {
            if(FirebaseAuth.getInstance().currentUser != null){
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMeusJogosFragment(), navOptions {
                    popUpTo(findNavController().graph.startDestinationId){
                        inclusive=true
                    }
                })
            }
            bindEvents()
        }
    }

    private fun bindEvents() {
        binding.loginButton.setOnClickListener { view ->
            val email = binding.loginEmailEditText.text.toString()
            val password = binding.loginPasswordEditText.text.toString()

            if(email.isEmpty() || password.isEmpty()){
                Snackbar.make(view, "Preencha todos os campos", Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(Color.GRAY)
                    .setTextColor(Color.BLACK)
                    .show()
            } else{
                viewModel.logar(email, password).observe(viewLifecycleOwner) { status ->
                    when(status) {
                        is MainViewModel.Status.Success -> {
                            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMeusJogosFragment(), navOptions {
                                popUpTo(findNavController().graph.startDestinationId){
                                    inclusive=true
                                }
                            })
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
        binding.cadastroTextView.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_cadastroFragment)
        }
        binding.esqueciSenhaTextView.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_recuperarSenhaFragment)
        }
    }
}