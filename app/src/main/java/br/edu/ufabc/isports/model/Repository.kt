package br.edu.ufabc.isports.model

import com.google.firebase.firestore.FirebaseFirestore

class Repository {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    companion object {
        private const val jogosCollection = "Jogos"

        private object TaskDoc {
            const val id = "id"
            const val title = "title"
            const val deadline = "deadline"
            const val tags = "tags"
            const val completed = "completed"
        }
    }

    private fun getJogosCollection() = db.collection(jogosCollection)

    fun addJogo(jogoFirestore: JogoFirestore) {
        getJogosCollection().add(jogoFirestore)
    }


}