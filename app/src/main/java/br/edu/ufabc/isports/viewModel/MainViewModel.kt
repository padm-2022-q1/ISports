package br.edu.ufabc.isports.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.edu.ufabc.isports.model.JogoFirestore

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val clickedItemId by lazy {
        MutableLiveData<JogoFirestore>()
    }
}