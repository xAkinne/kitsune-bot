package xyz.akinne.kitsune.listeners.commands.music;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.ArrayList;
import java.util.List;

public class exitCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("exit")){

            Guild guild = event.getGuild();
            Member member = event.getMember();
            VoiceChannel voiceChannel = (VoiceChannel) member.getVoiceState().getChannel();
            if (event.getChannel().asVoiceChannel() == voiceChannel){
                event.reply("ada").queue();
            }


        }




    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("exit","Exit from voice channel."));
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }
}
