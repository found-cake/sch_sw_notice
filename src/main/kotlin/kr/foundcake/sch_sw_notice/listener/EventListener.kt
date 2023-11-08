package kr.foundcake.sch_sw_notice.listener

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.foundcake.sch_sw_notice.database.DBManager
import kr.foundcake.sch_sw_notice.notice.NoticeService
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.channel.Channel
import net.dv8tion.jda.api.entities.channel.ChannelType
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.session.ShutdownEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.OptionMapping

class EventListener: ListenerAdapter() {

	private val scope = CoroutineScope(Dispatchers.IO)

	override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
		if(event.name == "중요공지") {
			event.reply("# 중요공지")
				.setEmbeds(NoticeService.createBigNoticeEmbeds())
				.setEphemeral(true).queue()
		} else if(event.name == "채널설정") {
			if(event.guild === null) {
				event.reply("서버에서만 사용가능한 명령어 입니다.")
					.setEphemeral(true).queue()
				return
			}
			if(event.member?.hasPermission(Permission.MANAGE_SERVER) != true){
				event.reply("권한이 없습니다")
					.setEphemeral(true).queue()
				return
			}
			val option: OptionMapping? = event.getOption("채널")
			if(option == null) {
				event.reply("/채널설정 <채널>")
					.setEphemeral(true).queue()
				return
			}
			val channel: Channel = option.asChannel
			if(channel.type !== ChannelType.TEXT) {
				event.reply("text 채널로 설정해야 합니다.")
					.setEphemeral(true).queue()
				return
			}
			scope.launch {
				DBManager.channel.registerChannel(event.guild!!.idLong, channel.idLong)
				event.reply("채널 설정이 완료되었습니다.").queue()
			}
		}
	}

	override fun onChannelDelete(event: ChannelDeleteEvent) {
		scope.launch { DBManager.channel.removeChannel(event.guild.idLong) }
	}

	override fun onShutdown(event: ShutdownEvent) {
		DBManager.close()
	}
}