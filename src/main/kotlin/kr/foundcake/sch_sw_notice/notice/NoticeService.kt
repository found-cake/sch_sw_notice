package kr.foundcake.sch_sw_notice.notice

import kr.foundcake.sch_sw_notice.database.DBManager

object NoticeService {

	fun createMainNoticeMessage() : String {
		var text = "# 중요 공지\n"
		DBManager.notice.getNotices().forEach {
			text += "- [${it.title}](${it.url}) - ${it.author}\n"
		}
		return text
	}
}