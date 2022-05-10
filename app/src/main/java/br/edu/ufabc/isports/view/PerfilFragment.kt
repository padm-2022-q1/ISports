package br.edu.ufabc.isports.view

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import br.edu.ufabc.isports.R
import br.edu.ufabc.isports.databinding.FragmentPerfilBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PerfilFragment : Fragment() {
    private lateinit var binding: FragmentPerfilBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPerfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        activity?.let{
            FirebaseFirestore.getInstance().collection("Usuarios")
                .document(FirebaseAuth.getInstance().currentUser!!.uid)
                .addSnapshotListener { value, _ ->
                    if(value != null){
                        binding.perfilUsername.text = value.getString("username")
                        binding.perfilEmail.text = FirebaseAuth.getInstance().currentUser!!.email
                    }
                }

            bindEvents()
        }
    }

    private fun bindEvents() {
        binding.bottomNavigationPerfil.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.menu_meus_jogos -> findNavController().navigate(R.id.action_perfilFragment_to_meusJogosFragment)
                R.id.menu_explorar -> findNavController().navigate(R.id.action_perfilFragment_to_explorarFragment)
            }
            true
        }
        binding.alterarCadastroButton.setOnClickListener { view ->
            FirebaseAuth.getInstance()
                .sendPasswordResetEmail(FirebaseAuth.getInstance().currentUser!!.email.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Snackbar.make(view, "Verifique sua caixa de email", Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(Color.GRAY)
                            .setTextColor(Color.BLACK)
                            .show()
                    } else {
                        val erro = when(task.exception!!){
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
        binding.logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(R.id.action_perfilFragment_to_loginFragment)
        }
    }
}