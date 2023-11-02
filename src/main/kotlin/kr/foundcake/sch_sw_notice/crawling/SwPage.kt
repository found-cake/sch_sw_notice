package kr.foundcake.sch_sw_notice.crawling

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

object SwPage: Page() {

	override fun url() = "https://home.sch.ac.kr/sw/07/010000.jsp?mode=list&board_no=20211209192056245082&pager.offset="

	 override fun getTbody(driver: WebDriver, page: Int) : MutableList<WebElement> {
		driver.get(createUrl(page))
		val tbody: WebElement = driver.findElement(By.xpath("//*[@id=\"sub_board\"]/div/div[1]/table/tbody"))
		return tbody.findElements(By.tagName("tr"))
	}
}