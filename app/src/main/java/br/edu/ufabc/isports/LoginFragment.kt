package br.edu.ufabc.isports

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import br.edu.ufabc.isports.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.*

class LoginFragment : Fragment(){
    private lateinit var  binding: FragmentLoginBinding

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
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful){
                            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMeusJogosFragment(), navOptions {
                                popUpTo(findNavController().graph.startDestinationId){
                                    inclusive=true
                                }
                            })
                        } else{
                            val erro = when(task.exception!!){
                                is FirebaseAuthInvalidCredentialsException -> {
                                    when(task.exception!!.message.toString().trim()){
                                        "The email address is badly formatted." -> "Endereço de email está mal formatado"
                                        "The password is invalid or the user does not have a password." -> "Senha incorreta"
                                        else -> "Erro ao autenticar usuário"
                                    }
                                }
                                is FirebaseAuthInvalidUserException -> "Usuário não encontrado"
                                else -> "Erro ao cadastrar usuário"
                            }
                            Snackbar.make(view, erro, Snackbar.LENGTH_SHORT)
                                .setBackgroundTint(Color.GRAY)
                                .setTextColor(Color.BLACK)
                                .show()
                        }
                    }
            }
        }
        binding.cadastroTextView.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_cadastroFragment)
        }
    }
}