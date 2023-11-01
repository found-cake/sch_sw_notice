package kr.foundcake.sch_sw_notice.database

import java.sql.Connection

object DBManager {

	private lateinit var conn: Connection

	private lateinit var channelDB: ChannelDB

	private lateinit var noticeDB: NoticeDB

	val channel: ChannelDB
		get() = channelDB

	val notice: NoticeDB
		get() = noticeDB

	val isClosed: Boolean
		get() =  conn.isClosed

	fun register(conn: Connection) {
		this.conn = conn
		channelDB = ChannelDB(conn)
		noticeDB = NoticeDB(conn)
	}

	fun close() {
		conn.close()
	}
}