package xyz.akinne.kitsune.listeners.commands;

import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

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
            event.reply("brakodpowiedzi niewysepuje").queue();

            event.getHook().sendMessage("test").queue();
        } else if (command.equals("testopcje")) {
            OptionMapping commoption = event.getOption("opcjanr1");
            String option = commoption.getAsString();

            event.getChannel().sendMessage(option).queue();
            event.reply("fin").setEphemeral(true).queue();

        }

    }


    @Override
    public void onGuildReady(GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        // zart
        commandData.add(Commands.slash("joke","daje zart"));
        //testerror
        commandData.add(Commands.slash("testerror","czeka dluzej na odpowiedz"));

        //opcjetest
        OptionData op1 = new OptionData(OptionType.STRING, "opcjanr1", "testtekst", true);
        commandData.add(Commands.slash("testopcje","ma kilkaopcji").addOptions(op1));

        event.getGuild().updateCommands().addCommands(commandData).queue();
    }



}
