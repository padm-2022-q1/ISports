package br.edu.ufabc.isports.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import br.edu.ufabc.isports.model.*
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.*
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
        data class AddParticipante(
            val value: Void?
        ) : Result()
        data class GetJogos(
            val value: List<Jogo>
        ) : Result()
        data class Logar(
            val value: AuthResult?
        ) : Result()
        data class RecuperarSenha(
            val value: Void?
        ) : Result()
        data class CriarUsuario(
            val value: Void?
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

    fun singOut() = repositoryAuth.singOut()

    fun recuperarSenha(email: String? = null) = liveData {
        try{
            emit(Status.Loading)
            emit(Status.Success(Result.RecuperarSenha(repositoryAuth.recuperarSenha(email ?: usuario.email))))
        } catch (e: FirebaseAuthInvalidCredentialsException){
            emit(Status.Failure(Exception("O campo de email está mal formatado", e)))
        } catch(e: FirebaseAuthInvalidUserException) {
            emit(Status.Failure(Exception("Não foi encontrado nenhum cadastro com esse email", e)))
        } catch (e: FirebaseNetworkException) {
            emit(Status.Failure(Exception("Falha na comunicação com o servidor, tente novamente mais tarde", e)))
        } catch(e: Exception) {
            emit(Status.Failure(Exception("Erro inesperado. Tente novamente mais tarde", e)))
        }
    }

    fun createUsuario(email: String, password: String, username: String) = liveData {
        try{
            emit(Status.Loading)
            repositoryAuth.createUser(email, password).user?.uid?.let{ uid ->
                emit(Status.Success(Result.CriarUsuario(repositoryFirestore.setUsername(uid, username))))
            }
        } catch(e: FirebaseAuthWeakPasswordException) {
            emit(Status.Failure(Exception("Digite uma senha com no minimo 6 caracteres", e)))
        } catch(e: FirebaseAuthUserCollisionException) {
            emit(Status.Failure(Exception("Esta conta já foi cadastrada", e)))
        } catch(e: FirebaseAuthInvalidCredentialsException) {
            emit(Status.Failure(Exception("E-mail inválido", e)))
        } catch(e: FirebaseNetworkException) {
            emit(Status.Failure(Exception("Falha na comunicação com o servidor, tente novamente mais tarde", e)))
        } catch(e: Exception) {
            emit(Status.Failure(Exception("Erro inesperado. Tente novamente mais tarde", e)))
        }
    }

    fun setUsuario() = liveData {
        try {
            emit(Status.Loading)
            repositoryAuth.getUsuarioLogado()?.let{ user ->
                val name = repositoryFirestore.getUsername(user.uid)
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

    fun addParticipante(uid: String) = liveData {
        try{
            emit(Status.Loading)
            emit(Status.Success(Result.AddParticipante(repositoryFirestore.addParticipante(uid, usuario.id, usuario.username))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failet to update element", e)))
        }
    }

    fun getJogosExplorar(modalidade: String) = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.GetJogos(repositoryFirestore.getJogosExplorar(modalidade, usuario.id))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failet to get element", e)))
        }
    }

    fun getMeusJogos() = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.GetJogos(repositoryFirestore.getMeusJogos(usuario.id, usuario.username))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failet to get element", e)))
        }
    }
}