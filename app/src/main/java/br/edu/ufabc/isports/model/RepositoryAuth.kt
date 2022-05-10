package br.edu.ufabc.isports.model

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class RepositoryAuth {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun logar(email: String, password: String): Task<AuthResult> {
        lateinit var op: Task<AuthResult>
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                op = it
            }
            .await()
        return op
    }

    fun getUsuarioLogado() = auth.currentUser
}