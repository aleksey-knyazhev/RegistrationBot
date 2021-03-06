package ru.registrationbot.model.entities

import ru.registrationbot.model.enums.TimeslotStatus
import java.time.LocalDate
import java.time.LocalTime
import javax.persistence.*

@Entity
@Table(name = "schedule", schema = "public", catalog = "RegistrationBot")
data class ScheduleEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var recordDate: LocalDate,

    var timeStart: LocalTime,

    var timeEnd: LocalTime,

    @Enumerated(EnumType.STRING)
    var status: TimeslotStatus,

    var client: Int? = null,
)


