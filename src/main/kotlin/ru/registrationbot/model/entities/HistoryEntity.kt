package ru.registrationbot.model.entities

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "history", schema = "public", catalog = "RegistrationBot")
data class HistoryEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Int? = null,

    @Column(name = "client", nullable = false)
    var client: Int,

    @Column(name = "date", nullable = false)
    var date: LocalDateTime?,

    @Column(name = "action", nullable = false)
    var action: String,

    @Column(name = "description", nullable = true)
    var description: String
)
