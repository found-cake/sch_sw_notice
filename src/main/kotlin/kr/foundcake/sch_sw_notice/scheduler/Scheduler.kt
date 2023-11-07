package kr.foundcake.sch_sw_notice.scheduler

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.foundcake.sch_sw_notice.database.DBManager
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.RemoteWebDriver
import java.sql.Connection
import java.sql.Statement
import java.util.Calendar
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

object Scheduler {
	private val scope = CoroutineScope(Dispatchers.IO)

	private val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(1)

	/**
	 * 매일 오후 8시에 실행시킬 task 등록
	 *
	 * @param worker
	 * @receiver
	 */
	fun registerDaily(worker: ((WebDriver) -> Unit)) {
		val calendar = Calendar.getInstance()
		calendar.set(Calendar.HOUR_OF_DAY, 20)
		calendar.set(Calendar.MINUTE, 0)
		calendar.set(Calendar.SECOND, 0)

		val currentTime = System.currentTimeMillis()
		val scheduledTime = calendar.timeInMillis

		val initialDelay = if (scheduledTime <= currentTime) {
			scheduledTime + TimeUnit.DAYS.toMillis(1) - currentTime
		} else {
			scheduledTime - currentTime
		}

		val period = TimeUnit.DAYS.toMillis(1)

		scheduler.scheduleAtFixedRate({
			scope.launch {
				val driver = RemoteWebDriver.builder()
					.address("http://selenium:4444/wd/hub")
					.addAlternative(ChromeOptions())
					.build()
				try {
					worker(driver)
				} catch (_: Throwable) {

				} finally {
					driver.quit()
				}
			}
		}, initialDelay, period, TimeUnit.MILLISECONDS)
	}

	/**
	 * Mysql connection 자동 close 되는 것을 막기 위함(30분 마다 실행)
	 *
	 * @param conn
	 */
	fun registerConn(conn: Connection) {
		scheduler.scheduleAtFixedRate({
			scope.launch {
				if(DBManager.isClosed) return@launch
 				val stmt: Statement = conn.createStatement()
				stmt.execute("select 1")
				stmt.close()
			}
		}, 30, 30, TimeUnit.MINUTES)
	}
}