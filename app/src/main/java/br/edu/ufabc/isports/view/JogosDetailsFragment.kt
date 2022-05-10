package br.edu.ufabc.isports.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import br.edu.ufabc.isports.databinding.FragmentJogosDetailsBinding

class JogosDetailsFragment : Fragment() {

    private lateinit var binding: FragmentJogosDetailsBinding
    private val args: JogosDetailsFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJogosDetailsBinding.inflate(inflater, container, false)
        bindEvents()
        return binding.root
    }

    private fun bindEvents()
    {
        args.jogoItem.takeIf { it?.id?.isNotEmpty() == true }?.also { jogoItem ->
            binding.textoTeste.text = jogoItem.id
        }
    }

}