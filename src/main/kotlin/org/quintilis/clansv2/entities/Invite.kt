package org.quintilis.clansv2.entities

import org.bson.types.ObjectId
import org.quintilis.clansv2.managers.ClanManager
import org.quintilis.clansv2.managers.PlayerManager
import org.quintilis.clansv2.string.bold
import java.util.Date
import java.util.UUID

data class Invite(
    val sender: UUID,
    val receiver: UUID,
    val clan: ObjectId,
    val sendDate: Date = Date(System.currentTimeMillis()),
    val expireDate: Date
) {
    override fun toString(): String {
        val sender = PlayerManager.getPlayerByMineId(sender)!!
        val receiver = PlayerManager.getPlayerByMineId(receiver)!!
        val clan = ClanManager.getClanById(clan)!!
        return "Remetente: ${sender.name.bold()}, Destinatário: ${receiver.name.bold()}, Clã: ${clan.name.bold()}"
    }
}
