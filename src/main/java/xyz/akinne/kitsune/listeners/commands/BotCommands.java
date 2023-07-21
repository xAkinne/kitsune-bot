package xyz.akinne.kitsune.listeners.commands;

import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.ArrayList;
import java.util.List;

public class BotCommands extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        String command = event.getName();

        if (command.equals("joke")){

            event.reply("Nie mam żartu dla ciebie!").queue();


        } else if (command.equals("testerror")) {
            event.deferReply().queue(); // pozwala na dłuższe czekanie na odpowiedź niż 3s

            event.getHook().sendMessage("test").queue();
        }

    }


    @Override
    public void onGuildReady(GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("joke","daje zart"));
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }



}
