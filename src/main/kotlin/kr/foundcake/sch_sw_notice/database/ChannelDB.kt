package kr.foundcake.sch_sw_notice.database

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

class ChannelDB(conn: Connection) {

	init {
		val stmt: Statement = conn.createStatement()
		stmt.execute(
			"CREATE TABLE IF NOT EXISTS `channel_db`(" +
			"    `serverId` BIGINT primary key," +
			"    `channelId` BIGINT" +
			")"
		)
		stmt.close()
	}

	private val registerChannelStmt: PreparedStatement = conn.prepareStatement(
		"INSERT INTO `channel_db`(`serverId`, `channelId`) VALUES(?, ?) ON DUPLICATE KEY UPDATE `channelId`=?"
	)

	fun registerChannel(serverId: Long, channelId: Long) : Boolean{
		synchronized(registerChannelStmt) {
			registerChannelStmt.setLong(1, serverId)
			registerChannelStmt.setLong(2, channelId)
			registerChannelStmt.setLong(3, channelId)
			return registerChannelStmt.execute()
		}
	}

	private val removeChannelByServerIdStmt: PreparedStatement = conn.prepareStatement(
		"DELETE FROM `channel_db` WHERE `serverId`=?"
	)

	fun removeChannelByServerId(serverId: Long): Boolean {
		synchronized(removeChannelByServerIdStmt) {
			removeChannelByServerIdStmt.setLong(1, serverId)
			return removeChannelByServerIdStmt.execute()
		}
	}

	private val removeChannelByChannelIdStmt: PreparedStatement = conn.prepareStatement(
		"DELETE FROM `channel_db` WHERE `serverId`=? and `channelId`=?"
	)

	fun removeChannelByChannelId(serverId: Long, channelId: Long) : Boolean{
		synchronized(removeChannelByChannelIdStmt) {
			removeChannelByChannelIdStmt.setLong(1, serverId)
			removeChannelByChannelIdStmt.setLong(2, channelId)
			return removeChannelByChannelIdStmt.execute()
		}
	}

	private val getChannelsStmt: PreparedStatement = conn.prepareStatement(
		"SELECT * FROM `channel_db`"
	)

	fun getChannels(): MutableList<Pair<Long, Long>> {
		val list: MutableList<Pair<Long, Long>> = mutableListOf()
		val result: ResultSet = getChannelsStmt.executeQuery()
		while(result.next()) {
			list.add(Pair(
				result.getLong("serverId"),
				result.getLong("channelId")
			))
		}
		return list
	}
}