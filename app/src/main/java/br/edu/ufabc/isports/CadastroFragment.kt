package br.edu.ufabc.isports
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.edu.ufabc.isports.databinding.FragmentCadastroBinding


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
            findNavController().navigate(R.id.action_cadastroFragment_to_meusJogosFragment)
        }
    }
}