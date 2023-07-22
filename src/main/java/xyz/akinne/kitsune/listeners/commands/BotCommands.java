package xyz.akinne.kitsune.listeners.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Widget;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.ArrayList;
import java.util.List;

public class BotCommands extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();

        // join ------------------------------------------------
        if (command.equals("join")){
            Guild guild = event.getGuild();
            Member member = event.getMember();
            VoiceChannel voiceChannel = (VoiceChannel) member.getVoiceState().getChannel();

            if (voiceChannel == null){
                event.reply("You must been on voice channel to use this commmand!").queue();
            }
            else {
                AudioManager audioManager = guild.getAudioManager();
                audioManager.openAudioConnection(voiceChannel);
                String voicechannel = voiceChannel.getId().toString();

                event.reply("Kitsue entering to: <#"+voicechannel+">...").queue();
            }



        }
        // leave -------------------------------------------------------------------
        else if (command.equals("exit")){

            Guild guild = event.getGuild();
            Member member = event.getMember();
            VoiceChannel voiceChannel = (VoiceChannel) member.getVoiceState().getChannel();
            AudioManager audioManager = guild.getAudioManager();

            if (voiceChannel == null){
                event.reply("You must been on voice channel to use this commmand!").queue();
            } else {
                audioManager.closeAudioConnection();
                String voicechannel = voiceChannel.getId().toString();

                event.reply("Kitsue leave from: <#"+voicechannel+">...").queue();
            }

        }

        else if (command.equals("leave")){

            Guild guild = event.getGuild();
            Member member = event.getMember();
            VoiceChannel voiceChannel = (VoiceChannel) member.getVoiceState().getChannel();
            AudioManager audioManager = guild.getAudioManager();

            if (voiceChannel == null){
                event.reply("You must been on voice channel to use this commmand!").queue();
            } else {
                audioManager.closeAudioConnection();
                String voicechannel = voiceChannel.getId().toString();

                event.reply("Kitsue leave from: <#"+voicechannel+">...").queue();
            }

        }

        //



    }







    @Override
    public void onGuildReady(GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("join","Join to voice channel."));
        commandData.add(Commands.slash("exit","Exit from voice channel."));
        commandData.add(Commands.slash("leave","Leave from voice channel."));
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }



}