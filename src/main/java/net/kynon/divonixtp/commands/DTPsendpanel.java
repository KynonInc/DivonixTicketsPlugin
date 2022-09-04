package net.kynon.divonixtp.commands;

import net.dv8tion.jda.api.entities.Channel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.kynon.divonixtp.classes.BetterEmbed;
import net.kynon.divonixtp.classes.BetterMessage;
import net.kynon.divonixtp.classes.Panel;

import java.io.File;

public class DTPsendpanel extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        if (event.getName().equalsIgnoreCase("DTPsendpanel")) {
            Member m = event.getMember();
            String name = event.getOption("name").toString();
            TextChannel channel = event.getOption("channel").getAsChannel().asTextChannel();
            Guild g = event.getGuild();

            if (new File("plugins/DTP/panels/" + name + ".yml").exists()) {
                Panel.send(name, channel, m, g);

                event.reply(BetterMessage.message("plugins/DTP/messages.yml", "panel-sent", m, g))
                        .addEmbeds(BetterEmbed.sendEmbed("plugins/DTP/messages.yml", "panel-sent", m, g).build())
                        .setEphemeral(true).queue();
            }
            else {
                event.reply(BetterMessage.message("plugins/DTP/messages.yml", "panel-doesnt-exist", m, g))
                        .addEmbeds(BetterEmbed.sendEmbed("plugins/DTP/messages.yml", "panel-doesnt-exist", m, g).build())
                        .setEphemeral(true).queue();
            }
        }
    }
}
