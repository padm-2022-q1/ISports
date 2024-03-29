package br.edu.ufabc.isports.model.objects

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Jogo(
    var id: String = "",
    var modalidade: String,
    var inicio: Date,
    var fim: Date,
    var local: String,
    var participantes: List<Participante>
) : Parcelable