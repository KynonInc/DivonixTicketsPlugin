package net.kynon.divonixtp.events;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.kynon.divonixtp.classes.Ticket;

public class TicketCreateEvent extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getComponentId().startsWith("DTPpanel")) {
            try {
                Guild g = event.getGuild();
                Member m = event.getMember();
                String name = event.getComponentId().replaceFirst("DTPpanel-", "");
                Ticket.create(name, m, g);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
