package net.kynon.divonixtp;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.kynon.divonix.plugins.DivonixPlugin;
import net.kynon.divonixtp.commands.DTPHelp;
import net.kynon.divonixtp.commands.DTPcreatepanel;
import net.kynon.divonixtp.commands.DTPdeletepanel;
import net.kynon.divonixtp.commands.DTPsendpanel;
import net.kynon.divonixtp.events.TicketCloseEvent;
import net.kynon.divonixtp.events.TicketCreateEvent;
import net.kynon.divonixtp.messages.MsgFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static net.kynon.divonix.Main.jda;

public class Main extends DivonixPlugin {

    public static String version = "1.1.0";

    @Override
    public void onEnable() {
        new File("plugins/DTP").mkdirs();
        new File("plugins/DTP/panels").mkdirs();
        new File("plugins/DTP/tickets").mkdirs();

        try {
            MsgFile.loadMsg();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        jda.addEventListener(new DTPHelp());
        jda.addEventListener(new DTPsendpanel());
        jda.addEventListener(new DTPcreatepanel());
        jda.addEventListener(new DTPdeletepanel());
        jda.addEventListener(new TicketCreateEvent());
        jda.addEventListener(new TicketCloseEvent());

        jda.upsertCommand("dtphelp", "View all commands of Divonix Tickets Plugin").queue();
        jda.upsertCommand("dtpcreatepanel", "Create a tickets panel").addOption(OptionType.STRING, "name", "panel's name", true).queue();
        jda.upsertCommand("dtdeletepanel", "Delete a tickets panel").addOption(OptionType.STRING, "name", "panel's name", true).queue();
        jda.upsertCommand("dtpsendpanel", "Send a panel in your current channel").addOption(OptionType.STRING, "name", "panel's name", true).addOption(OptionType.CHANNEL, "channel", "channel's ID where the panel will be sent", true).queue();

        System.out.println("[DTP] Thanks for using Divonix Tickets Plugin!");
        System.out.println("[DTP] You can view all of the commands by writing /dtphelp");
    }


    public static Map<String, Object> replacePlaceholders(Map<String, Object> input, Member member, Guild guild, String special, String specialinput) {

        Map<String, Object> output = new HashMap<>();

        for (String key : input.keySet()) {
            if (input.get(key) != null) {
                String newkey = input.get(key).toString().replaceAll("%member-tag%", member.getUser().getAsTag())
                        .replaceAll("%member-name%", member.getUser().getName())
                        .replaceAll("%member-mention%", "<@!" + member.getId() + ">")
                        .replaceAll("%member-id%", member.getId())
                        .replaceAll("%total-members%", String.valueOf(guild.getMemberCount()))
                                .replaceAll("%bot-version%", version);

                if (member.getNickname() != null) {
                    newkey = newkey.replaceAll("%member-nickname%", member.getNickname());
                }
                else {
                    newkey = newkey.replaceAll("%member-nickname%", "none");
                }

                if (member.getAvatarUrl() != null) {
                    newkey = newkey.replaceAll("%member-avatar-url%", member.getUser().getAvatarUrl());
                }
                else {
                    newkey = newkey.replaceAll("%member-avatar-url%", "");
                }

                if (Objects.equals(special, "ticket-created")) {
                    newkey = newkey.replaceAll("%channel%", specialinput);
                }

                output.put(key, newkey);
            }
        }

        return output;
    }
}