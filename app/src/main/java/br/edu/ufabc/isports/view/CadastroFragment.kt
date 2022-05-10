package br.edu.ufabc.isports.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import br.edu.ufabc.isports.databinding.FragmentCadastroBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore


class CadastroFragment : Fragment() {
    private lateinit var binding: FragmentCadastroBinding

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
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        FirebaseFirestore.getInstance().collection("Usuarios")
                            .document(FirebaseAuth.getInstance().currentUser!!.uid)
                            .set(mapOf("username" to username))
                        findNavController().navigate(CadastroFragmentDirections.actionCadastroFragmentToMeusJogosFragment(), navOptions {
                            popUpTo(findNavController().graph.startDestinationId){
                                inclusive=true
                            }
                        })
                    } else{
                        val erro = when(task.exception!!){
                            is FirebaseAuthWeakPasswordException -> "Digite uma senha com no minimo 6 caracteres"
                            is FirebaseAuthUserCollisionException -> "Esta conta já foi cadastrada"
                            is FirebaseAuthInvalidCredentialsException -> "E-mail inválido"
                            is FirebaseNetworkException -> "Falha na comunicação com o servidor, tente novamente mais tarde"
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
    }
}