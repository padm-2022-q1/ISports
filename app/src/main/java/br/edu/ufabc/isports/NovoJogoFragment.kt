package br.edu.ufabc.isports

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.edu.ufabc.isports.databinding.FragmentNovoJogoBinding

class NovoJogoFragment : Fragment() {
    private lateinit var binding: FragmentNovoJogoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNovoJogoBinding.inflate(inflater, container, false)
        return binding.root
    }
}