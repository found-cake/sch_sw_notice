package kr.foundcake.sch_sw_notice.crawling

import kr.foundcake.sch_sw_notice.notice.Notice
import org.openqa.selenium.By
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.WebDriver

object MainPage: Page(){

	override fun url() = "https://home.sch.ac.kr/sch/06/010100.jsp?mode=list&board_no=20090723152156588979&pager.offset="

	override fun xpath(): String = "//*[@id=\"contents_wrap\"]/div/div[1]/table/tbody"

	fun getBigNotices(driver: WebDriver) : MutableList<Notice> {
		val list: MutableList<Notice> = mutableListOf()
		getTbody(driver, 1).forEach {
			try {
				it.findElement(By.className("notice"))
				list.add(elementParser(it))
			} catch (e: NoSuchElementException) {
				return@forEach
			}
		}
		return list
	}
}