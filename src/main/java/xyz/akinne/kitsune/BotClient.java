package xyz.akinne.kitsune;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import xyz.akinne.kitsune.listeners.EventListener;
import xyz.akinne.kitsune.listeners.commands.BotCommands;


public class BotClient {

    private final Dotenv config;
    private final ShardManager shardManager;

    public BotClient(){
        config = Dotenv.configure().load();
        String token = config.get("TOKEN");
        String ingame = config.get("INGAME");

        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.listening(ingame));

        shardManager = builder.build();


            shardManager.addEventListener(new EventListener(),new BotCommands());

    }

    public Dotenv getConfig(){
        return config;
    }

    public ShardManager getShardManager(){
        return shardManager;
    }

    public static void main(String[] args) { BotClient bot = new BotClient(); }



}
