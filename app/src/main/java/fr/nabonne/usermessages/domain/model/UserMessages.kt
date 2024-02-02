package fr.nabonne.usermessages.domain.model

//data class UserMessages(val user: String, val posts: List<Message>)
data class Message(val subject: String, val content: String, val author: String)