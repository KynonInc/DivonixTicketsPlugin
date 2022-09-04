package net.kynon.divonixtp.classes;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.kynon.divonixtp.Main;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;

public class Ticket {

    public static void create(String name, Member m, Guild g) throws IOException {
        String number = String.valueOf(new File("plugins/DTP/tickets/" + name).listFiles().length + 1);

        BufferedWriter bw = new BufferedWriter(new FileWriter("plugins/DTP/tickets/" + name + "/" + number + ".yml"));

        Yaml yaml = new Yaml();
        Map<String, Object> panelData = Main.replacePlaceholders(yaml.load(new FileInputStream("plugins/DTP/panels/" + name + ".yml")), m, g);
        Category category = g.getCategoryById(panelData.get("panel-category").toString());

        String ticketname = panelData.get("tickets-names").toString().replaceAll("%number%", number);

        List<Permission> perms = new ArrayList<>();
        perms.add(Permission.VIEW_CHANNEL);
        perms.add(Permission.MESSAGE_SEND);
        perms.add(Permission.MESSAGE_ATTACH_FILES);

        TextChannel channel = category.createTextChannel(ticketname).addMemberPermissionOverride(m.getIdLong(), perms, Collections.emptyList()).complete();

        bw.write("state: open");
        bw.write("channel: " + channel.getId());
        bw.write("created-by: " + m.getId());
        bw.close();

        channel.sendMessage(BetterMessage.message("plugins/DTP/panels/" + name + ".yml", "ticket", m, g))
                .addEmbeds(BetterEmbed.sendEmbed("plugins/DTP/panels/" + name + ".yml", "ticket", m, g).build())
                .addActionRow(Button.primary("DTPcloseticket-" + name, "❌")).queue();
    }

    public static void close(String channelID, String name, Member m, Guild g) throws IOException {
        Yaml yaml = new Yaml();

        for (File f : new File("plugins/DTP/tickets/" + name).listFiles()) {
            Map<String, Object> ticketData = yaml.load(new FileInputStream(f));
            Map<String, Object> panelData = yaml.load(new FileInputStream("plugins/DTP/panels/" + name + ".yml"));

            if (ticketData.get("channel").toString().equals(channelID)) {
                String number = f.getName().replaceAll(".yml", "");
                String member = ticketData.get("created-by").toString();

                BufferedWriter bw = new BufferedWriter(new FileWriter("plugins/DTP/tickets/" + name + "/" + number + ".yml"));
                bw.write("state: closed");
                bw.write("channel: " + channelID);
                bw.write("created-by: " + member);
                bw.close();

                g.getTextChannelById(channelID).sendMessage(BetterMessage.message("plugins/DTP/panels/" + name + ".yml", "ticket-closing", m, g))
                        .addEmbeds(BetterEmbed.sendEmbed("plugins/DTP/panels/" + name + ".yml", "ticket-closing", m, g).build()).queue();

                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        g.getTextChannelById(channelID).delete().queue();
                    }
                };

                int time = Integer.parseInt(panelData.get("ticket-closing-time").toString());

                Timer timer = new Timer();
                timer.schedule(task, time * 1000L);
            }
        }
    }
}