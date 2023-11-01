package kr.foundcake.sch_sw_notice.notice

enum class NoticeStatus {
	NEW_DATA,
	REMOVE_DATA,
	STABLE;

	companion object {
		fun fromInt(value: Int): NoticeStatus {
			return when(value) {
				-1 -> REMOVE_DATA
				0 -> STABLE
				1 -> NEW_DATA
				else -> REMOVE_DATA
			}
		}
	}

	fun toInt() : Int{
		return when(this){
			NEW_DATA -> 1
			REMOVE_DATA -> -1
			STABLE -> 0
		}
	}
}