package br.edu.ufabc.isports.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.edu.ufabc.isports.R
import br.edu.ufabc.isports.databinding.FragmentRecuperarSenhaBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class RecuperarSenhaFragment : Fragment(){
    private lateinit var  binding: FragmentRecuperarSenhaBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecuperarSenhaBinding.inflate(inflater, container, false)
        return binding.root
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
                FirebaseAuth.getInstance()
                    .sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Snackbar.make(view, "Verifique sua caixa de email", Snackbar.LENGTH_SHORT)
                                .setBackgroundTint(Color.GRAY)
                                .setTextColor(Color.BLACK)
                                .show()
                            findNavController().navigate(R.id.action_recuperarSenhaFragment_to_loginFragment)
                        } else {
                            val erro = when(task.exception!!){
                                is FirebaseAuthInvalidCredentialsException -> "O campo de email está mal formatado"
                                is FirebaseAuthInvalidUserException -> "Não foi encontrado nenhum cadastro com esse email"
                                is FirebaseNetworkException -> "Falha na comunicação com o servidor, tente novamente mais tarde"
                                else -> "Erro inesperado. Tente novamente mais tarde"
                            }
                            Snackbar.make( view,erro, Snackbar.LENGTH_SHORT)
                                .setBackgroundTint(Color.GRAY)
                                .setTextColor(Color.BLACK)
                                .show()
                        }
                    }
            }
        }
    }
}