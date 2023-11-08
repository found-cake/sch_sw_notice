package kr.foundcake.sch_sw_notice.crawling

import kr.foundcake.sch_sw_notice.notice.Notice
import org.openqa.selenium.By
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration

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

	protected abstract fun xpath() : String

	protected fun getTbody(driver: WebDriver, page: Int) : MutableList<WebElement> {
		driver.get(createUrl(page))
		val wait = WebDriverWait(driver, Duration.ofSeconds(10))
		var tbody: WebElement? = null
		wait.until {
			tbody =  it.findElement(By.xpath(xpath()))
		}
		return tbody?.findElements(By.tagName("tr")) ?: mutableListOf()
	}

	fun getNewNotices(driver: WebDriver, page: Int = 1) : MutableList<Notice> {
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
			list += getNewNotices(driver, page + 1)
		}
		return list
	}
}