package br.edu.ufabc.isports.model

data class Jogos(
    var id: Long,
    var modalidade: String,
    var dateCreated: String,
    var timeStart: String,
    var timeEnd: String,
    var endereco: String
)