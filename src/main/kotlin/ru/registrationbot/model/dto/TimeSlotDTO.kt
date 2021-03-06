package ru.registrationbot.model.dto

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class TimeSlotDTO(var idRecording: Long,
                  var recordDate: LocalDate,
                  var timeStart: LocalTime,
                  var timeEnd: LocalTime,
                  var firstName: String?
) {
    override fun toString(): String {
        return "${recordDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))}\t$timeStart-$timeEnd. " +
                "Отменить запись: /cancel\\_$idRecording"
    }
}