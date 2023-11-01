package kr.foundcake.sch_sw_notice.database

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.Statement

class ChannelDB(conn: Connection) {

	init {
		val stmt: Statement = conn.createStatement()
		stmt.execute(
			"CREATE TABLE IF NOT EXISTS `channel_db`(" +
			"    `serverId` VARCHAR(20) primary key," +
			"    `channelId` VARCHAR(20)" +
			")"
		)
		stmt.close()
	}

	private val registerChannelStmt: PreparedStatement = conn.prepareStatement(
		"INSERT INTO `channel_db`(`serverId`, `channelId`) VALUES(?, ?) ON DUPLICATE KEY UPDATE `channelId`=?"
	)

	fun registerChannel(serverId: String, channelId: String) : Boolean{
		registerChannelStmt.setString(1, serverId)
		registerChannelStmt.setString(2, channelId)
		registerChannelStmt.setString(3, channelId)
		return registerChannelStmt.execute()
	}

	private val removeChannelStmt: PreparedStatement = conn.prepareStatement(
		"DELETE FROM `channel_db` WHERE `serverId`=?"
	)

	fun removeChannel(serverId: String) {
		removeChannelStmt.setString(1, serverId)
		removeChannelStmt.execute()
	}
}