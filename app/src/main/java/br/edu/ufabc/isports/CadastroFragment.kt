package br.edu.ufabc.isports

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navOptions
import br.edu.ufabc.isports.databinding.FragmentCadastroBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth


class CadastroFragment : Fragment() {
    private lateinit var binding: FragmentCadastroBinding
    private  val _args : CadastroFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCadastroBinding.inflate(inflater, container, false)
        if(_args.isAlteracao){
            binding.cadastroEmailEditText.hint = "admin@admin.com"
            binding.cadastroEmailEditText.isEnabled = false
            binding.cadastroUsernameEditText.hint = "admin"
            binding.cadastroUsernameEditText.isEnabled = false
            binding.cadastroPasswordEditText.hint = "New Password"
            binding.cadastroButton.text = getString(R.string.alterar_cadastro_text)
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        activity?.let {
            bindEvents()
        }
    }

    private fun bindEvents() {
        binding.cadastroButton.setOnClickListener {
            val email = binding.cadastroEmailEditText.text.toString()
            val username = binding.cadastroUsernameEditText.text.toString()
            val password = binding.cadastroPasswordEditText.text.toString()
            val confirmPassword = binding.cadastroConfirmPasswordEditText.text.toString()

            if(email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
                Snackbar.make(it, "Preencha todos os campos", Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(Color.GRAY)
                    .setTextColor(Color.BLACK)
                    .show()
            } else if(password != confirmPassword){
                Snackbar.make(it, "As senhas não batem", Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(Color.GRAY)
                    .setTextColor(Color.BLACK)
                    .show()
            } else{
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        findNavController().navigate(CadastroFragmentDirections.actionCadastroFragmentToMeusJogosFragment(), navOptions {
                            popUpTo(findNavController().graph.startDestinationId){
                                inclusive=true
                            }
                        })
                    }
                }
            }
        }
    }
}