package br.edu.ufabc.isports.model.objects

data class Usuario(
    var id: String,
    var email: String,
    var username: String,
    var meusJogos: List<Jogo>,
    var historico: List<Jogo>
)