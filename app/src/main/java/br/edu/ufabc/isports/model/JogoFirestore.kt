package br.edu.ufabc.isports.model

import java.util.Date

data class JogoFirestore(
    var id: String,
    var modalidade: String,
    var inicio: Date,
    var fim: Date,
    var local: String
)