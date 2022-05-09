package br.edu.ufabc.isports

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.edu.ufabc.isports.model.JogoFirestore

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = (application as App).repository

    val clickedItemId by lazy {
        MutableLiveData<JogoFirestore>()
    }

    fun allContacts() = repository.getAll()

    fun getAt(position: Int) = repository.getAt(position)

    fun getById(id: Long) = repository.getById(id)
}