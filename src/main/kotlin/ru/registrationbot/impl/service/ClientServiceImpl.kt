package ru.registrationbot.impl.service

import org.springframework.stereotype.Service
import ru.registrationbot.api.service.ClientService
import ru.registrationbot.api.service.DBServiceAnswer
import ru.registrationbot.dto.UserInfo
import ru.registrationbot.entities.ClientsEntity
import ru.registrationbot.enum.TimeslotStatus
import javax.transaction.Transactional

@Service
class ClientServiceImpl(private val repositoryTime: SchedulerRepository,
                        private val repositoryClient: ClientRepository
) : ClientService {

    @Transactional
    override fun addRecording(idRecording: Long, user: UserInfo):Boolean {
        val client = repositoryClient.findByChatId(user.chatId)
        val clientId =
                    if (!client.isPresent)
                    {
                        val newClient = ClientsEntity(
                            phone = "01234567", //todo это нужно где-то взять
                            chatId = user.chatId,
                            userName = user.userName,
                            firstName = user.firstName)
                        repositoryClient.save(newClient).id!!
                    }
                    else
                    {client.get().id}

        val record = repositoryTime.findById(idRecording)
        return if (record.isPresent && TimeslotStatus.FREE == record.get().status)
                {
                    record.get().status = TimeslotStatus.BOOKED
                    record.get().client = clientId

                    repositoryTime.save(record.get())
                    true
                }
                else
                { false }
    }


    @Transactional
    override fun deleteRecording(idRecording: Long):Int? {
        val record = repositoryTime.findById(idRecording)

        if (!record.isPresent ) //TODO нужно ли проверять статус на занятость слота?
        {
            return null
        }

        record.get().status = TimeslotStatus.FREE
        val clientChatId = record.get().client
        repositoryTime.save(record.get())
        return clientChatId
    }

    override fun confirmRecording(userInfo: UserInfo) = changeStatusTimeSlot(userInfo, TimeslotStatus.CONFIRMED)

    override fun cancelRecording(userInfo: UserInfo) = changeStatusTimeSlot(userInfo, TimeslotStatus.FREE)

    private fun changeStatusTimeSlot(userInfo: UserInfo, status: TimeslotStatus):DBServiceAnswer {

        val client = repositoryClient.findByChatId(userInfo.chatId)

        if (!client.isPresent) {
            return DBServiceAnswer.CLIENT_NOT_FOUND
        }

        val record = repositoryTime.findByClient(client.get().id!!)
        if (!record.isPresent)
            return DBServiceAnswer.RECORD_NOT_FOUND

        record.get().status = status
        repositoryTime.save(record.get())

        return DBServiceAnswer.SUCCESS
    }
}