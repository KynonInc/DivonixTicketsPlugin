package net.kynon.divonixtp.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.kynon.divonixtp.classes.BetterEmbed;
import net.kynon.divonixtp.classes.BetterMessage;
import net.kynon.divonixtp.classes.Panel;

import java.io.File;

public class DTPcreatepanel extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        if (event.getName().equalsIgnoreCase("dtpcreatepanel")) {
            Member m = event.getMember();
            String name = event.getOption("name").toString();
            Guild g = event.getGuild();

            if (!new File("plugins/DTP/panels/" + name + ".yml").exists()) {
                new File("plugins/DTP/tickets/" + name).mkdirs();

                Panel.create(name);

                event.reply(BetterMessage.message("plugins/DTP/messages.yml", "panel-created", m, g))
                        .addEmbeds(BetterEmbed.sendEmbed("plugins/DTP/messages.yml", "panel-created", m, g).build())
                        .setEphemeral(true).queue();
            }
            else {
                event.reply(BetterMessage.message("plugins/DTP/messages.yml", "panel-already-exists", m, g))
                        .addEmbeds(BetterEmbed.sendEmbed("plugins/DTP/messages.yml", "panel-already-exists", m, g).build())
                        .setEphemeral(true).queue();
            }
        }
    }
}
