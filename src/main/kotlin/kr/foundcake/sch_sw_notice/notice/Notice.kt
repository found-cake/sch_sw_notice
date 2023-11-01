package kr.foundcake.sch_sw_notice.notice

data class Notice(
	val title: String,
	val url: String,
	val author: String
) {
	override fun toString(): String {
		return "Notice($title $author)"
	}
}
