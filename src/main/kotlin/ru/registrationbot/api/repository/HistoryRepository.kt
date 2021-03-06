package ru.registrationbot.api.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.registrationbot.model.entities.HistoryEntity

@Repository
interface HistoryRepository: CrudRepository<HistoryEntity, Long> {

    fun findByClient(clientId: Int): List <HistoryEntity>

}