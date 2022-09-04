package net.kynon.divonixtp.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.kynon.divonixtp.Main;

public class DTPHelp extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        Member m = event.getMember();

        if (event.getName().equalsIgnoreCase("DTPhelp")) {
            EmbedBuilder eb = new EmbedBuilder();

            eb.setTitle("All commands for Divonix Tickets");
            eb.addField("/DTPcreatepanel <name>", "create a new tickets panel", false);
            eb.addField("/DTPsendpanel <panel-name> <channel-ID>", "send specified tickets panel in a channel", false);

            eb.setFooter("DTP | v" + Main.version);

            event.replyEmbeds(eb.build()).setEphemeral(true);
        }
    }
}
