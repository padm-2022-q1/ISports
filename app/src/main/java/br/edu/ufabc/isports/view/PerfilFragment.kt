package br.edu.ufabc.isports.view

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import br.edu.ufabc.isports.R
import br.edu.ufabc.isports.databinding.FragmentPerfilBinding
import br.edu.ufabc.isports.viewModel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class PerfilFragment : Fragment() {
    private lateinit var binding: FragmentPerfilBinding
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPerfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = ProgressBar(binding.progressHorizontal)
    }

    override fun onStart() {
        super.onStart()
        activity?.let{
            binding.perfilUsername.text = viewModel.usuario.username
            binding.perfilEmail.text = viewModel.usuario.email
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
            viewModel.recuperarSenha().observe(viewLifecycleOwner) { status ->
                when(status) {
                    is MainViewModel.Status.Loading -> progressBar.start()
                    is MainViewModel.Status.Success -> {
                        progressBar.stop()
                        Snackbar.make(view, "Verifique sua caixa de email", Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(Color.GRAY)
                            .setTextColor(Color.BLACK)
                            .show()
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
        binding.logoutButton.setOnClickListener {
            progressBar.start()
            viewModel.singOut()
            findNavController().navigate(R.id.action_perfilFragment_to_loginFragment)
        }
    }
}