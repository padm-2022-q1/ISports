package br.edu.ufabc.isports.model

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class RepositoryAuth {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun getUsuarioLogado() = auth.currentUser

    suspend fun logar(email: String, password: String) =
        auth.signInWithEmailAndPassword(email, password).await()

    fun singOut() =
        auth.signOut()

    suspend fun recuperarSenha(email: String) =
        auth.sendPasswordResetEmail(email).await()
}