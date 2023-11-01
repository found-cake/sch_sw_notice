package kr.foundcake.sch_sw_notice.listener

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.channel.Channel
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.OptionMapping

class EventListener: ListenerAdapter() {

	override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
		if(event.name == "중요공지") {
			event.reply("중요공지를 출력할 예정입니다.").setEphemeral(true).queue()
		} else if(event.name == "채널설정") {
			if(event.member?.hasPermission(Permission.MANAGE_CHANNEL) != true){
				event.reply("권한이 없습니다").setEphemeral(true).queue()
			}
			val option: OptionMapping? = event.getOption("채널")
			if(option == null) {
				event.reply("/채널설정 <채널>").setEphemeral(true).queue()
				return
			}
			val channel: Channel = option.asChannel!!
			//TODO: 서버, 채널 정보 db에 저장

			event.reply("${event.channelId}, ${channel.id}설정 되었습니다.").queue()
		}
	}

	override fun onChannelDelete(event: ChannelDeleteEvent) {
		event.channel
	}
}