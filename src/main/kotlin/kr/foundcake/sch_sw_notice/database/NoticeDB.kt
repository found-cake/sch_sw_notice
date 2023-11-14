package kr.foundcake.sch_sw_notice.database

import kr.foundcake.sch_sw_notice.notice.Notice
import kr.foundcake.sch_sw_notice.notice.NoticeStatus
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

class NoticeDB(conn: Connection) {

	init {
		val stmt: Statement = conn.createStatement()
		stmt.execute(
			"CREATE TABLE IF NOT EXISTS `notice_db`(" +
			"    `title` VARCHAR(100)," +
			"    `url` VARCHAR(250)," +
			"    `author` VARCHAR(50)," +
			"    `status` TINYINT" +
			")"
		)
		stmt.close()
	}

	private val addNoticeStmt: PreparedStatement = conn.prepareStatement(
		"INSERT INTO `notice_db`(`title`, `url`, `author`, `status`)" +
				"VALUES (?, ?, ?, ${NoticeStatus.NEW_DATA.toInt()})"
	)

	fun addNotice(notice: Notice) : Boolean {
		synchronized(addNoticeStmt) {
			addNoticeStmt.setString(1, notice.title)
			addNoticeStmt.setString(2, notice.url)
			addNoticeStmt.setString(3, notice.author)
			return addNoticeStmt.execute()
		}
	}

	private val removeNoticeStmt: PreparedStatement = conn.prepareStatement(
		"DELETE FROM `notice_db` WHERE `status`=${NoticeStatus.REMOVE_DATA.toInt()}"
	)

	fun removeNotice(): Boolean = removeNoticeStmt.execute()

	private val fetchStatusStmt: PreparedStatement = conn.prepareStatement(
		"UPDATE `notice_db` SET `status`=`status`-1"
	)

	fun fetchStatus(): Boolean = fetchStatusStmt.execute()

	private val getNoticesStmt: PreparedStatement = conn.prepareStatement(
		"SELECT `title`, `url`, `author` FROM `notice_db` WHERE status=${NoticeStatus.STABLE.toInt()}"
	)

	fun getNotices() : MutableList<Notice> {
		val list: MutableList<Notice> = mutableListOf()
		val result: ResultSet = getNoticesStmt.executeQuery()
		while (result.next()) {
			list.add(Notice(
				result.getString("title"),
				result.getString("url"),
				result.getString("author")
			))
		}
		return list
	}
}