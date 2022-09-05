package net.kynon.divonixtp.events;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.kynon.divonixtp.classes.BetterEmbed;
import net.kynon.divonixtp.classes.BetterMessage;
import net.kynon.divonixtp.classes.Ticket;

public class TicketCreateEvent extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getComponentId().startsWith("DTPpanel")) {
            try {
                Guild g = event.getGuild();
                Member m = event.getMember();
                String name = event.getComponentId().replaceFirst("DTPpanel-", "");

                TextChannel channel = Ticket.create(name, m, g);

                event.reply(BetterMessage.message("plugins/DTP/panels/" + name + ".yml", "ticket-created", m, g, "ticket-created", channel.getAsMention()))
                        .addEmbeds(BetterEmbed.sendEmbed("plugins/DTP/panels/" + name + ".yml", "ticket-created", m, g, "ticket-created", channel.getAsMention()).build())
                        .setEphemeral(true).queue();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
