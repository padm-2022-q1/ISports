package br.edu.ufabc.isports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.edu.ufabc.isports.databinding.FragmentLoginBinding

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
            bindEvents()
        }
    }

    private fun bindEvents() {
        binding.loginButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_meusJogosFragment)
        }
    }
}