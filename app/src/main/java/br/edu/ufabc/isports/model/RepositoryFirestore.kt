package br.edu.ufabc.isports.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class RepositoryFirestore {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    companion object {
        private const val jogosCollection = "Jogos"
        private const val usuarioCollection = "Usuarios"

        private object JogoDoc {
            const val modalidade = "modalidade"
            const val inicio = "inicio"
            const val fim = "fim"
            const val local = "local"
        }

        private object UsuarioDoc {
            const val nome = "username"
        }
    }

    private fun getJogosCollection() = db.collection(jogosCollection)
    private fun getUsuariosCollection() = db.collection(usuarioCollection)

    fun addJogo(jogoFirestore: JogoFirestore) =
        getJogosCollection().add(jogoFirestore)

    suspend fun getJogos(modalidade: String): List<Jogo> {
        val list: MutableList<Jogo> = mutableListOf()
        val query = when(modalidade) {
            "Todos" -> getJogosCollection()
            else -> getJogosCollection()
                .whereEqualTo(JogoDoc.modalidade, modalidade)
        }
        query
            .get()
            .await()
            .documents.let { documents ->
                for (document in documents) {
                    list.add(Jogo(
                        document.id,
                        document.data!![JogoDoc.modalidade].toString(),
                        (document.data!![JogoDoc.inicio] as Timestamp).toDate(),
                        (document.data!![JogoDoc.fim] as Timestamp).toDate(),
                        document.data!![JogoDoc.local].toString()))
                }
            }
        return list
    }

    suspend fun getNameUser(uid: String): String  = getUsuariosCollection()
        .document(uid)
        .get()
        .await()
        .data!![UsuarioDoc.nome]
        .toString()
}