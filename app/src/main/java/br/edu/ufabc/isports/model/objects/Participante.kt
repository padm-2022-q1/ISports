package br.edu.ufabc.isports.model.objects

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Participante(
    var uid: String,
    var username: String
) : Parcelable
