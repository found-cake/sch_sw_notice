package kr.foundcake.sch_sw_notice.listener

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.foundcake.sch_sw_notice.database.DBManager
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.channel.Channel
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.OptionMapping

class EventListener: ListenerAdapter() {

	private val scope = CoroutineScope(Dispatchers.IO)

	override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
		if(event.guild === null) return
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
			val channel: Channel = option.asChannel
			scope.launch {
				if(DBManager.channel.registerChannel(event.guild!!.id, channel.id)){
					event.reply("채널 설정이 완료되었습니다.").queue()
				} else {
					event.reply("채널 설정을 실패했습니다.\n다시 시도해주세요.(계속될 시 문의)").setEphemeral(true).queue()
				}
			}
		}
	}

	override fun onChannelDelete(event: ChannelDeleteEvent) {
		scope.launch { DBManager.channel.removeChannel(event.guild.id) }
	}
}