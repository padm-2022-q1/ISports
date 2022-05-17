package br.edu.ufabc.isports.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import br.edu.ufabc.isports.databinding.FragmentEsperaBinding
import br.edu.ufabc.isports.viewModel.MainViewModel

class EsperarFragment : Fragment(){
    private lateinit var  binding: FragmentEsperaBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEsperaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        activity?.let {
            viewModel.setUsuario().observe(viewLifecycleOwner) { status ->
                when(status){
                    is MainViewModel.Status.Success -> {
                        (status.result as MainViewModel.Result.SetUsuario).value.let {
                            if(it == null){
                                findNavController().navigate(
                                    EsperarFragmentDirections.actionEsperarFragmentToLoginFragment(),
                                    navOptions {
                                        popUpTo(findNavController().graph.startDestinationId){
                                            inclusive=true
                                        }
                                    }
                                )
                            } else {
                                findNavController().navigate(
                                    EsperarFragmentDirections.actionEsperarFragmentToMeusJogosFragment(),
                                    navOptions {
                                        popUpTo(findNavController().graph.startDestinationId){
                                            inclusive=true
                                        }
                                    }
                                )
                            }
                        }
                    }
                    else -> { }
                }
            }
        }
    }
}