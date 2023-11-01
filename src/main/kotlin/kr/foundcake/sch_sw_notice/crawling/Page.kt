package kr.foundcake.sch_sw_notice.crawling

import kr.foundcake.sch_sw_notice.notice.Notice
import org.openqa.selenium.By
import org.openqa.selenium.WebElement

abstract class Page {
	protected abstract fun url(): String

	protected fun createUrl(page: Int) : String = "${url()}${(page - 1) * 10}"

	protected fun elementParser(tr: WebElement) : Notice {
		val a: WebElement = tr.findElement(By.tagName("a"))
		return Notice(
			title = a.text,
			url = a.getAttribute("href"),
			author = tr.findElement(By.className("writer")).text
		)
	}
}