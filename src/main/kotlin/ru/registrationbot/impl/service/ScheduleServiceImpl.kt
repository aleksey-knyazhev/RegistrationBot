package ru.registrationbot.impl.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.registrationbot.api.service.SchedulerService
import ru.registrationbot.model.entities.ScheduleEntity
import ru.registrationbot.model.enums.TimeslotStatus
import ru.registrationbot.api.repository.ScheduleRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Service
class ScheduleServiceImpl(
    private val scheduleRepository: ScheduleRepository
) : SchedulerService {

    @Transactional
    override fun openRecording(date: LocalDate, time: String) {
        val regex = Regex("\\d{2}:\\d{2} \\d{2}:\\d{2}")
        if (!regex.matches(time)) {
            throw Exception("Неверный формат времени \"10:00 18:00\"")
        } else if (date < LocalDate.now()) {
            throw Exception("Дата прошла")
        }
        val twoDates = time.split(" ")
        var currentStartTime = LocalTime.parse(twoDates.first())
        val shiftEnd = LocalTime.parse(twoDates.last())
        while (shiftEnd.minusHours(1L) >= currentStartTime) {
            scheduleRepository.save(
                ScheduleEntity(
                    recordDate = date,
                    timeStart = currentStartTime,
                    timeEnd = currentStartTime.plusHours(1L),
                    status = TimeslotStatus.FREE
                )
            )
            currentStartTime = currentStartTime.plusHours(1)
        }
    }

    override fun getDates() = scheduleRepository
        .findByStatus(TimeslotStatus.FREE)
        .filter { it.recordDate >= LocalDate.now() }
        .map { it.recordDate }.distinct().sorted()

    override fun getTimesForDate(date: LocalDate) = scheduleRepository
        .findByStatusAndRecordDate(TimeslotStatus.FREE, date)
        .filter { LocalDateTime.of(date, it.timeStart) > LocalDateTime.now() }
        .map { "${it.recordDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))}  ${it.timeStart}-${it.timeEnd}" }.sorted()
}