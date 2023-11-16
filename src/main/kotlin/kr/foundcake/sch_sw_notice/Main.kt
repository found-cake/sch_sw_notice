package kr.foundcake.sch_sw_notice

import kr.foundcake.sch_sw_notice.crawling.MainPage
import kr.foundcake.sch_sw_notice.crawling.SwPage
import kr.foundcake.sch_sw_notice.database.DBManager
import kr.foundcake.sch_sw_notice.listener.EventListener
import kr.foundcake.sch_sw_notice.notice.NoticeService
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

	Scheduler.registerDaily { driver ->
		MainPage.getBigNotices(driver).forEach { DBManager.notice.addNotice(it) }
		DBManager.notice.fetchStatus()
		DBManager.notice.removeNotice()
		NoticeService.sendNotice(
			jda,
			MainPage.getNewNotices(driver),
			SwPage.getNewNotices(driver)
		)
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
			.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MANAGE_SERVER))
			.addOption(OptionType.CHANNEL, "채널","지정할 채널", true),
		Commands.slash("sw공지설정", "순천향대학교 SW 중심 대학 산업단 공지를 출력할지 설정합니다.")
			.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MANAGE_SERVER))
			.addOption(OptionType.BOOLEAN, "toggle", "없을 경우 현재 설정에서 반대가 기본값입니다."),
		Commands.slash("공지색상설정", "공지 Embed 색상을 설정 합니다.")
			.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MANAGE_SERVER))
			.addOption(OptionType.STRING, "공지종류", "중요공지|포털공지|SW공지", true)
			.addOption(OptionType.INTEGER, "red", "0~255", true)
			.addOption(OptionType.INTEGER, "green", "0~255", true)
			.addOption(OptionType.INTEGER, "blue", "0~255", true)
	).queue()

	return jda
}