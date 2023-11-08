package kr.foundcake.sch_sw_notice.crawling

object SwPage: Page() {

	override fun url() = "https://home.sch.ac.kr/sw/07/010000.jsp?mode=list&board_no=20211209192056245082&pager.offset="

	override fun xpath(): String = "//*[@id=\"sub_board\"]/div/div[1]/table/tbody"
}