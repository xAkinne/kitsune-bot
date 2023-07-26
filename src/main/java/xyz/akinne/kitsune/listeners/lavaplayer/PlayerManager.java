package xyz.akinne.kitsune.listeners.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManager {

    private String playlistName;
    private String playlistSize;
    private static PlayerManager INSTANCE;

    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    public PlayerManager(){
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);

    }


    public GuildMusicManager getMusicManager(Guild guild){

        return this.musicManagers.computeIfAbsent(guild.getIdLong(),(guildId) ->{

            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);


            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());

            return guildMusicManager;
        });

    }

    public void loadAndPlay(TextChannel channel, String trackUrl){
        final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());

        this.audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                musicManager.scheduler.queue(track);

            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {

                final List<AudioTrack> tracks = audioPlaylist.getTracks();

                playlistName = audioPlaylist.getName();
                playlistSize = String.valueOf(tracks.size());



                if (isYtSearch(trackUrl)){
                    for (AudioTrack track : tracks){
                        musicManager.scheduler.queue(track);
                        break;
                    }
                }else{
                    for (AudioTrack track : tracks){
                        musicManager.scheduler.queue(track);
                    }
                }


            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException e) {

            }
        });
    }






    public static PlayerManager getInstance(){
        if (INSTANCE == null){
            INSTANCE = new PlayerManager();
        }
        return INSTANCE;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public String getPlaylistSize() {
        return playlistSize;
    }


    private boolean isYtSearch(String text) {
        return text.matches("ytsearch:.*");
    }

}
