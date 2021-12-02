package ru.registrationbot.entities

import javax.persistence.*

@Entity
@Table(name = "schedule", schema = "public", catalog = "RegistrationBot")
open class ScheduleEntity {
    @Id
    @Column(name = "id", nullable = false)
    var id: Long = 0

    @Column(name = "timeStart", nullable = false)
    var timeStart: java.sql.Timestamp? = null

    @Column(name = "timeEnd", nullable = false)
    var timeEnd: java.sql.Timestamp? = null

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    lateinit var status: State

    @Column(name = "client", nullable = true)
    var client: Long? = null


    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "id = $id " +
                "timeStart = $timeStart " +
                "timeEnd = $timeEnd " +
                "status = $status " +
                "client = $client " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ScheduleEntity

        if (id != other.id) return false
        if (timeStart != other.timeStart) return false
        if (timeEnd != other.timeEnd) return false
        if (status != other.status) return false
        if (client != other.client) return false

        return true
    }

}

enum class State{
    FREE,
    BUSY,
    CONFIRM
}


