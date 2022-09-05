package net.kynon.divonixtp.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.kynon.divonixtp.classes.BetterEmbed;
import net.kynon.divonixtp.classes.BetterMessage;

import java.io.File;

public class DTPdeletepanel extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        if (event.getName().equalsIgnoreCase("dtpdeletepanel")) {
            Member m = event.getMember();
            String name = event.getOption("name").getAsString();
            Guild g = event.getGuild();

            if (new File("plugins/DTP/panels/" + name + ".yml").exists()) {
                new File("plugins/DTP/panels/" + name + ".yml").delete();
                new File("plugins/DTP/tickets/" + name).delete();

                event.reply(BetterMessage.message("plugins/DTP/messages.yml", "panel-deleted", m, g, "", ""))
                        .addEmbeds(BetterEmbed.sendEmbed("plugins/DTP/messages.yml", "panel-deleted", m, g, "", "").build())
                        .setEphemeral(true).queue();
            }
            else {
                event.reply(BetterMessage.message("plugins/DTP/messages.yml", "panel-doesnt-exist", m, g, "", ""))
                        .addEmbeds(BetterEmbed.sendEmbed("plugins/DTP/messages.yml", "panel-doesnt-exist", m, g, "", "").build())
                        .setEphemeral(true).queue();
            }
        }
    }
}
