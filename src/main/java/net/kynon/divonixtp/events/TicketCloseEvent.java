package net.kynon.divonixtp.events;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.kynon.divonixtp.classes.BetterEmbed;
import net.kynon.divonixtp.classes.BetterMessage;
import net.kynon.divonixtp.classes.Ticket;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.util.Map;

public class TicketCloseEvent extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getComponentId().startsWith("DTPcloseticket")) {
            try {
                Guild g = event.getGuild();
                Member m = event.getMember();
                String id = event.getComponentId().replaceFirst("DTPcloseticket-", "");
                String name = id.substring(0 , id.indexOf(";"));
                String number = id.substring(id.lastIndexOf(";") + 1);

                Yaml yaml = new Yaml();
                Map<String, Object> ticketData = yaml.load(new FileInputStream("plugins/DTP/tickets/" + name + "/" + number + ".yml"));

                if (!ticketData.get("state").equals("closed")) {
                    Ticket.close(number, name, m, g);

                    event.reply(BetterMessage.message("plugins/DTP/panels/" + name + ".yml", "ticket-closing", m, g, "", ""))
                            .addEmbeds(BetterEmbed.sendEmbed("plugins/DTP/panels/" + name + ".yml", "ticket-closing", m, g, "", "").build()).queue();
                }
                else {
                    event.reply(BetterMessage.message("plugins/DTP/panels/" + name + ".yml", "ticket-already-closed", m, g, "", ""))
                            .addEmbeds(BetterEmbed.sendEmbed("plugins/DTP/panels/" + name + ".yml", "ticket-already-closed", m, g, "", "").build())
                            .setEphemeral(true).queue();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
