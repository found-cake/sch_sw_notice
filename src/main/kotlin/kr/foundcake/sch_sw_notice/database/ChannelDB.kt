package kr.foundcake.sch_sw_notice.database

import java.sql.Connection
import java.sql.Statement

class ChannelDB(conn: Connection) {

	init {
		val stmt: Statement = conn.createStatement()
		stmt.execute("" +
				"CREATE TABLE IF NOT EXISTS `channel_db`(" +
				"    `serverId` VARCHAR(20) primary key," +
				"    `channelId` VARCHAR(20)" +
				")"
		)
		stmt.close()
	}

	private val registerChannel = conn.prepareStatement("")
}