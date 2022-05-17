package br.edu.ufabc.isports.model.repository

import br.edu.ufabc.isports.model.objects.Jogo
import br.edu.ufabc.isports.model.objects.Participante
import br.edu.ufabc.isports.model.dto.JogoDTO
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class RepositoryFirestore {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    companion object {
        private const val jogosCollection = "Jogos"
        private const val usuarioCollection = "Usuarios"

        private object JogoDoc {
            const val  id = "id"
            const val modalidade = "modalidade"
            const val inicio = "inicio"
            const val participantes = "participantes"
        }

        private object UsuarioDoc {
            const val nome = "username"
        }
    }

    private fun getJogosCollection() = db.collection(jogosCollection)
    private fun getUsuariosCollection() = db.collection(usuarioCollection)

    suspend fun addJogo(jogo: Jogo) : Void? {
        getJogosCollection().add(jogo).await().let { doc ->
            return doc.update(JogoDoc.id, doc.id).await()
        }
    }

    suspend fun getJogosExplorar(modalidade: String, uid: String, dtDe: String, dtAte: String): List<Jogo> {
        val hoje = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val query = when(modalidade) {
            "Todos" -> getJogosCollection()
            else -> getJogosCollection()
                .whereEqualTo(JogoDoc.modalidade, modalidade)
        }
        return query
            .whereGreaterThan(JogoDoc.inicio, hoje)
            .orderBy(JogoDoc.inicio)
            .get()
            .await()
            .toObjects(JogoDTO::class.java)
            .map { it.toJogo() }
            .filter { jogo ->
                !jogo.participantes.any { participante -> participante.uid == uid } &&
                        (dtDe.isEmpty() || sdf.parse(dtDe)!! <= jogo.inicio) &&
                        (dtAte.isEmpty() || sdf.parse(sdf.format(jogo.inicio))!! <= sdf.parse(dtAte)!!)
            }
    }

    suspend fun getMeusJogos(uid: String, username: String): List<Jogo> {
        val hoje = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())
        return getJogosCollection()
            .whereGreaterThan(JogoDoc.inicio, hoje)
            .whereArrayContains(JogoDoc.participantes, Participante(uid, username))
            .orderBy(JogoDoc.inicio)
            .get()
            .await()
            .toObjects(JogoDTO::class.java)
            .map { it.toJogo() }
    }

    suspend fun getHistorico(uid: String, username: String): List<Jogo> {
        val hoje = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())
        return getJogosCollection()
            .whereLessThan(JogoDoc.inicio, hoje)
            .whereArrayContains(JogoDoc.participantes, Participante(uid, username))
            .orderBy(JogoDoc.inicio, Query.Direction.DESCENDING)
            .get()
            .await()
            .toObjects(JogoDTO::class.java)
            .map { it.toJogo() }
    }

    suspend fun addParticipante(uid: String, userId: String, username: String): Void? = getJogosCollection().document(uid)
        .update(JogoDoc.participantes, FieldValue.arrayUnion(Participante(userId, username)))
        .await()

    suspend fun  removeParticipante(uid: String, userId: String, username: String): Void? = getJogosCollection().document(uid)
        .update(JogoDoc.participantes, FieldValue.arrayRemove(Participante(userId, username)))
        .await()

    suspend fun setUsername(uid: String, username: String): Void? = getUsuariosCollection()
        .document(uid)
        .set(mapOf("username" to username))
        .await()

    suspend fun getUsername(uid: String): String  = getUsuariosCollection()
        .document(uid)
        .get()
        .await()
        .data!![UsuarioDoc.nome]
        .toString()
}