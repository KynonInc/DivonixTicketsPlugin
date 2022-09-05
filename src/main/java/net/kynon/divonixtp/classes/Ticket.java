package net.kynon.divonixtp.classes;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.kynon.divonixtp.Main;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;

public class Ticket {

    public static TextChannel create(String name, Member m, Guild g) throws IOException {
        String number = String.valueOf(new File("plugins/DTP/tickets/" + name).listFiles().length);

        BufferedWriter bw = new BufferedWriter(new FileWriter("plugins/DTP/tickets/" + name + "/" + number + ".yml"));

        Yaml yaml = new Yaml();
        Map<String, Object> panelData = Main.replacePlaceholders(yaml.load(new FileInputStream("plugins/DTP/panels/" + name + ".yml")), m, g, "", "");
        Category category = g.getCategoryById(panelData.get("panel-category").toString());

        String ticketname = panelData.get("tickets-names").toString().replaceAll("%number%", number);

        List<Permission> perms = new ArrayList<>();
        perms.add(Permission.VIEW_CHANNEL);
        perms.add(Permission.MESSAGE_SEND);
        perms.add(Permission.MESSAGE_ATTACH_FILES);

        TextChannel channel = category.createTextChannel(ticketname).addMemberPermissionOverride(m.getIdLong(), perms, Collections.emptyList()).complete();

        bw.write("state: open\n");
        bw.write("channel: " + channel.getId() + "\n");
        bw.write("created-by: " + m.getId());
        bw.close();

        channel.sendMessage(BetterMessage.message("plugins/DTP/panels/" + name + ".yml", "ticket", m, g, "", ""))
                .addEmbeds(BetterEmbed.sendEmbed("plugins/DTP/panels/" + name + ".yml", "ticket", m, g, "", "").build())
                .addActionRow(Button.secondary("DTPcloseticket-" + name + ";" + number, "❌")).queue();

        return channel;
    }

    public static void close(String number, String name, Member m, Guild g) throws IOException {
        Yaml yaml = new Yaml();
        Map<String, Object> ticketData = yaml.load(new FileInputStream("plugins/DTP/tickets/" + name + "/" + number + ".yml"));
        Map<String, Object> panelData = yaml.load(new FileInputStream("plugins/DTP/panels/" + name + ".yml"));

        String member = ticketData.get("created-by").toString();
        String channelid = ticketData.get("channel").toString();

        String history = "";
        for (Message msg : g.getTextChannelById(channelid).getHistory().retrievePast(g.getTextChannelById(channelid).getHistory().size()).complete()) {
            history+="(" + msg.getAuthor().getId() + ") " + msg.getAuthor().getAsTag() + ": " + msg.getContentRaw() + "\n";
        }

        BufferedWriter bw = new BufferedWriter(new FileWriter("plugins/DTP/tickets/" + name + "/logs/" + number + ".yml"));
        bw.write(history);
        bw.close();

        BufferedWriter bw2 = new BufferedWriter(new FileWriter("plugins/DTP/tickets/" + name + "/" + number + ".yml"));
        bw2.write("state: closed\n");
        bw2.write("channel: " + channelid + "\n");
        bw2.write("created-by: " + member);
        bw2.close();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                g.getTextChannelById(channelid).delete().queue();
            }
        };
        int time = Integer.parseInt(panelData.get("ticket-closing-time").toString());
        Timer timer = new Timer();
        timer.schedule(task, time * 1000L);
    }
}