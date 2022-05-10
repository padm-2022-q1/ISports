package br.edu.ufabc.isports.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import br.edu.ufabc.isports.model.Jogo
import br.edu.ufabc.isports.model.JogoFirestore
import br.edu.ufabc.isports.model.Repository
import java.lang.Exception

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = Repository()

    val clickedItemId by lazy {
        MutableLiveData<Jogo>()
    }

    fun addJogo(jogoFirestore: JogoFirestore) = liveData {
        try {
            emit(repository.addJogo(jogoFirestore))
        } catch (e: Exception) {
            emit(Exception("Failet to add element", e))
        }
    }
}