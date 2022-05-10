package br.edu.ufabc.isports.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import br.edu.ufabc.isports.model.*
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import java.lang.Exception

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repositoryFirestore = RepositoryFirestore()
    private val repositoryAuth = RepositoryAuth()
    lateinit var usuario: Usuario

    sealed class Result {
        data class SetUsuario(
            val value: Usuario?
        ) : Result()
        data class AddJogo(
            val value: Boolean
        ) : Result()
        data class GetJogos(
            val value: List<Jogo>
        ) : Result()
        data class Logar(
            val value: Task<AuthResult>
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

    fun logar(email: String, password: String) = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.Logar(repositoryAuth.logar(email, password))))
        } catch(e: FirebaseAuthInvalidCredentialsException) {
            val message = when(e.message.toString().trim()){
                "The email address is badly formatted." -> "Endereço de email está mal formatado"
                "The password is invalid or the user does not have a password." -> "Senha incorreta"
                else -> "Erro ao autenticar usuário"
            }
            emit(Status.Failure(Exception(message, e)))
        } catch(e: FirebaseAuthInvalidUserException) {
            emit(Status.Failure(Exception("Usuário não encontrado", e)))
        } catch(e: FirebaseNetworkException) {
            emit(Status.Failure(Exception("Falha na comunicação com o servidor, tente novamente mais tarde", e)))
        } catch(e: Exception) {
            emit(Status.Failure(Exception("Erro ao logar usuário", e)))
        }
    }

    fun setUsuario() = liveData {
        try {
            emit(Status.Loading)
            repositoryAuth.getUsuarioLogado()?.let{ user ->
                val name = repositoryFirestore.getNameUser(user.uid)
                usuario = Usuario(user.uid, user.email!!, name)
                emit(Status.Success(Result.SetUsuario(usuario)))
            }
            emit(Status.Success(Result.SetUsuario(null)))
        } catch(e: Exception) {
            emit(Status.Failure(Exception("Failet to get user", e)))
        }
    }

    fun addJogo(jogoFirestore: JogoFirestore) = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.AddJogo(repositoryFirestore.addJogo(jogoFirestore).isSuccessful)))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failet to add element", e)))
        }
    }

    fun getJogos(modalidade: String) = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.GetJogos(repositoryFirestore.getJogos(modalidade))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failet to get element", e)))
        }
    }
}