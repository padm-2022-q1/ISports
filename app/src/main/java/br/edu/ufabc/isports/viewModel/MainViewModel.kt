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

    sealed class Result {
        data class AddJogo(
            val value: Boolean
        ) : Result()
        data class GetJogos(
            val value: List<Jogo>
        ) : Result()
    }

    sealed class Status {
        class Failure(val e: Exception) : Status()
        class Success(val result: Result) : Status()
        object Loading : Status()
    }

    val clickedItemId by lazy {
        MutableLiveData<Jogo>()
    }

    fun addJogo(jogoFirestore: JogoFirestore) = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.AddJogo(repository.addJogo(jogoFirestore).isSuccessful)))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failet to add element", e)))
        }
    }

    fun getJogos(modalidade: String) = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.GetJogos(repository.getJogos(modalidade))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failet to get element", e)))
        }
    }
}