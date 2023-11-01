package kr.foundcake.sch_sw_notice.database

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.Statement

class NoticeDB(conn: Connection) {

	init {
		val stmt: Statement = conn.createStatement()
		stmt.execute(
			"CREATE TABLE IF NOT EXISTS `notice_db`(" +
			"    `title` VARCHAR(100) PRIMARY KEY," +
			"    `url` VARCHAR(250)," +
			"    `author` VARCHAR(50)," +
			"    `status` TINYINT" +
			")"
		)
		stmt.close()
	}


}