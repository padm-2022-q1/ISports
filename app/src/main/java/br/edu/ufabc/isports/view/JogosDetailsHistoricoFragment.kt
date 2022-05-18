package br.edu.ufabc.isports.view

import android.view.View

class JogosDetailsHistoricoFragment : JogosDetailsFragment(){
    override fun removePlayer() {
        binding.fabParticipar.visibility = View.INVISIBLE
    }
}