package kr.foundcake.sch_sw_notice.crawling

import kr.foundcake.sch_sw_notice.notice.Notice
import org.openqa.selenium.By
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.WebDriver
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

	protected abstract fun getTbody(driver: WebDriver, page: Int) : MutableList<WebElement>

	fun getNewAlert(driver: WebDriver, page: Int = 1) : MutableList<Notice> {
		val list: MutableList<Notice> = mutableListOf()
		var needNext = true
		getTbody(driver, page).forEach {
			try {
				it.findElement(By.className("new"))
				list.add(elementParser(it))
			} catch (e: NoSuchElementException) {
				needNext = false
				return@forEach
			}
		}
		if(needNext) {
			list += getNewAlert(driver, page + 1)
		}
		return list
	}
}