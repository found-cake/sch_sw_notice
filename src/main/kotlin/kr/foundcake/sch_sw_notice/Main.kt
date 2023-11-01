package kr.foundcake.sch_sw_notice

import kr.foundcake.sch_sw_notice.crawling.MainPage
import kr.foundcake.sch_sw_notice.database.DBManager
import kr.foundcake.sch_sw_notice.listener.EventListener
import kr.foundcake.sch_sw_notice.scheduler.Scheduler
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import java.sql.Connection
import java.sql.DriverManager

fun main() {
	settingDB()

	val jda: JDA = runBot()

	Scheduler.registerDaily {
		MainPage.getBigAlert(it).forEach { println(it) }
		MainPage.getNewAlert(it)
	}

	jda.awaitReady()
}

fun settingDB() {
	Class.forName("com.mysql.cj.jdbc.Driver")

	val envs: Map<String, String> = System.getenv()
	val url = "jdbc:mysql://mysql:3306/${envs["DB_NAME"]}"
	val user = "root"
	val password: String = envs["DB_PASSWORD"]!!

	val conn: Connection = DriverManager.getConnection(url, user, password)
	DBManager.register(conn)
	Scheduler.registerConn(conn)
}

fun runBot() : JDA {
	val token = System.getenv("DISCORD_TOKEN")
	val jda: JDA = JDABuilder.createDefault(token)
		.addEventListeners(EventListener())
		.build()

	jda.updateCommands().addCommands(
		Commands.slash("중요공지", "고정된 공지들을 출력합니다."),
		Commands.slash("채널설정", "최신 공지들을 출력할 채널을 설정합니다.")
			.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MANAGE_CHANNEL))
			.addOption(OptionType.CHANNEL, "채널","지정할 채널", true)
	).queue()

	return jda
}