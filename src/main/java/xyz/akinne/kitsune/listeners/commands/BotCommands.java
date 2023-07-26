package xyz.akinne.kitsune.listeners.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import xyz.akinne.kitsune.listeners.lavaplayer.GuildMusicManager;
import xyz.akinne.kitsune.listeners.lavaplayer.PlayerManager;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Widget;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.managers.AudioManager;
import xyz.akinne.kitsune.listeners.lavaplayer.PlayerManager;

import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;

public class BotCommands extends ListenerAdapter {



    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();
        EmbedBuilder embed = new EmbedBuilder();
        EmbedClear(embed);


        // help
        if (command.equals("help")){
            embed.setAuthor("Akinne", "http://akinne.xyz/","http://akinne.xyz/index_pliki/image01.jpg");
            embed.setTitle("**Kitsune Help:**", null);
            embed.setColor(new Color(0xFF7700));
            embed.setDescription("Test");

            event.replyEmbeds(embed.build()).queue();


        }

        else if (command.equals("ping")){
            event.reply("Pong!").queue();


        }


        // join ------------------------------------------------
        else if (command.equals("join")){
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

                GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());

                musicManager.scheduler.player.stopTrack();
                musicManager.scheduler.queue.clear();



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

        // play -------------------------------------------------------------

        else if (command.equals("play")){



            final TextChannel channel = event.getChannel().asTextChannel();
            final Member member = event.getMember();
            final GuildVoiceState memberVoiceState = member.getVoiceState();


            Guild guild = event.getGuild();
            AudioChannel memberAudioChannel = member.getVoiceState().getChannel();
            AudioManager audioManager = guild.getAudioManager();


            if (!memberVoiceState.inAudioChannel()){
                event.deferReply().queue();
                event.getHook().sendMessage("You must be in a voice channel to use this command!").queue();
            } else {
                VoiceChannel voiceChannel = (VoiceChannel) member.getVoiceState().getChannel();
                if (!audioManager.isConnected()) {
                    audioManager.openAudioConnection(voiceChannel);
                }
                event.deferReply().queue();

                String url = String.join(" ",event.getOption("url").getAsString());




                if (!isUrl(url)){
                    url = "ytsearch:"+ url;
                }

                Boolean isPlaylist = containsPlaylistLink(url);



                PlayerManager.getInstance().loadAndPlay(channel,url);
                PlayerManager.getInstance().getPlaylistName();
                PlayerManager.getInstance().getPlaylistSize();

                if (isPlaylist){
                    event.getHook().sendMessage("**Added **`"+PlayerManager.getInstance().getPlaylistSize()+"` ** tracks from playlist** `"+PlayerManager.getInstance().getPlaylistName()+"` **to queue.**" ).queue();
                } else {
                    event.getHook().sendMessage("**Added to queue:** *"+url+"*" ).queue();
                }








            }




        }


        // stop -------------------------------------------------------------

        else if (command.equals("stop")){


            final TextChannel channel = event.getChannel().asTextChannel();
            final Member member = event.getMember();
            final GuildVoiceState memberVoiceState = member.getVoiceState();

            if (!memberVoiceState.inAudioChannel()){
                event.deferReply().queue();
                event.getHook().sendMessage("You must be in a voice channel to use this command!").queue();
            } else{
                event.deferReply().queue();

                GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());

                musicManager.scheduler.player.stopTrack();
                musicManager.scheduler.queue.clear();

                event.getHook().sendMessage("**Kitsune stopped.**" ).queue();


            }


        }


        // skip -------------------------------------------------------------
        else if (command.equals("skip")){
            final TextChannel channel = event.getChannel().asTextChannel();
            final Member member = event.getMember();
            final GuildVoiceState memberVoiceState = member.getVoiceState();

            if (!memberVoiceState.inAudioChannel()){
                event.deferReply().queue();
                event.getHook().sendMessage("You must be in a voice channel to use this command!").queue();
            } else{
                event.deferReply().queue();

                final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
                final AudioPlayer audioPlayer = musicManager.audioPlayer;

                if (audioPlayer.getPlayingTrack() == null){
                    event.getHook().sendMessage("**Currently there is 0 track on queue.**").queue();
                } else {
                    musicManager.scheduler.nexTrack();
                    event.getHook().sendMessage("**Skipped.**" ).queue();

                }


            }


        }

        // nowplay ------------------------------------------------------------------------------

        else if (command.equals("np")){
            final TextChannel channel = event.getChannel().asTextChannel();
            final Member member = event.getMember();
            final GuildVoiceState memberVoiceState = member.getVoiceState();

            if (!memberVoiceState.inAudioChannel()){
                event.deferReply().queue();
                event.getHook().sendMessage("You must be in a voice channel to use this command!").queue();
            } else{
                event.deferReply().queue();

                final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
                final AudioPlayer audioPlayer = musicManager.audioPlayer;
                final AudioTrack track = audioPlayer.getPlayingTrack();

                AudioTrackInfo info = track.getInfo();
                event.getHook().sendMessageFormat("**Now playing** `%s` **by** `%s` \n**Link:** %s", info.title, info.author, info.uri).queue();


            }


        }


        // queue ---------------------------

        else if (command.equals("queue")) {
            GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
            BlockingQueue<AudioTrack> queue = musicManager.scheduler.queue;

            if (queue.isEmpty()){
                event.reply("**The queue is currently empty.**");
            }else {
                event.reply("**comming Soon**");
            }

        }

        // loop ----------------------------

        else if (command.equals("loop")){

            final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
            final boolean newLoop = !musicManager.scheduler.loop;
            event.replyFormat("**Loop is set to: **`%s`",newLoop ? "true" : "false").queue();


        }



    }







    @Override
    public void onGuildReady(GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("join","Join to voice channel."));
        commandData.add(Commands.slash("exit","Exit from voice channel."));
        commandData.add(Commands.slash("leave","Leave from voice channel."));
        commandData.add(Commands.slash("help","Send help message."));
        commandData.add(Commands.slash("ping","Let's play Ping-Pong!"));

        commandData.add(Commands.slash("play", "Play a song in the voice channel.")
                .addOption(OptionType.STRING, "url", "URL or title of the song to play.", true));

        commandData.add(Commands.slash("stop","Stop all playing music."));
        commandData.add(Commands.slash("skip","Skip current track."));
        commandData.add(Commands.slash("np","Show now playing track."));
        commandData.add(Commands.slash("loop","Setup loop."));

        event.getGuild().updateCommands().addCommands(commandData).queue();
    }












    private void EmbedClear(EmbedBuilder embed) {
        embed.setTitle(null);
        embed.setDescription("");
        embed.clearFields();
        embed.setImage(null);
        embed.setThumbnail(null);
        embed.setFooter(null, null);

    }



    private boolean isUrl(String url){
        try{
            new URI(url);
            return true;
        } catch (URISyntaxException e){
            return false;
        }


    }

    private boolean containsPlaylistLink(String text) {
        return text.matches(".*playlist\\?list=.*");
    }




}