package br.edu.ufabc.isports

import android.app.Application
import br.edu.ufabc.isports.model.Repository

class App : Application() {
    companion object {
        private var contactsFile = "jogos.json"
    }

    val repository = Repository()

    override fun onCreate() {
        super.onCreate()

        resources.assets.open(contactsFile).use {
            repository.loadData(it)
        }
    }
}