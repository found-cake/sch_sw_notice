package kr.foundcake.sch_sw_notice.notice

enum class NoticeStatus {
	NEW_DATA,
	REMOVE_DATA,
	STABLE;

	fun toInt() : Int{
		return when(this){
			REMOVE_DATA -> -1
			STABLE -> 0
			NEW_DATA -> 1
		}
	}
}