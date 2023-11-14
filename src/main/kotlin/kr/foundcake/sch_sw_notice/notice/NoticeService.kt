package kr.foundcake.sch_sw_notice.notice

import kr.foundcake.sch_sw_notice.database.DBManager
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel
import java.awt.Color

object NoticeService {

	private fun createEmbed(notice: Notice, color: Color = Color.white) : MessageEmbed {
		return EmbedBuilder()
			.setTitle(notice.title)
			.addField(notice.author, "[바로가기](${notice.url})", true)
			.setColor(color)
			.build()
	}

	fun createBigNoticeEmbeds(): MutableList<MessageEmbed>{
		val list: MutableList<MessageEmbed> = mutableListOf()
		DBManager.notice.getNotices().forEach { list.add(createEmbed(it)) }
		return list
	}

	fun sendNotice(
		jda: JDA,
		mainNotices: MutableList<Notice>,
		swNotices: MutableList<Notice>
	) {
		val channels: MutableList<Pair<Long, Long>> = DBManager.channel.getChannels()
		if(channels.isEmpty() || (mainNotices.isEmpty() && swNotices.isEmpty()))
			return

		val mainList: MutableList<MessageEmbed> = mutableListOf()
		val swList: MutableList<MessageEmbed> = mutableListOf()

		mainNotices.forEach {
			mainList.add(createEmbed(it, Color(160, 233, 255)))
		}
		swNotices.forEach {
			swList.add(createEmbed(it, Color.green))
		}

		channels.forEach {
			val serverId: Long = it.first
			val channelId: Long = it.second
			val channel: GuildChannel? = jda.getGuildChannelById(channelId)
			if(channel === null || channel !is TextChannel) {
				DBManager.channel.removeChannelByServerId(serverId)
				return@forEach
			}
			if(mainList.isNotEmpty()) {
				channel.sendMessage("# 포털공지")
					.setEmbeds(mainList)
					.queue()
			}
			if(swList.isNotEmpty()) {
				channel.sendMessage("# SW융합대학공지")
					.setEmbeds(swList)
					.queue()
			}
		}
	}
}