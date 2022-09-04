package net.kynon.divonixtp.classes;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.kynon.divonixtp.Main;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Panel {

    public static void create(String name) {
        String filename = "panel.yml";
        ClassLoader classLoader = Main.class.getClassLoader();

        try (InputStream inputStream = classLoader.getResourceAsStream(filename)) {
            String result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

            File file = new File("plugins/DTP/panels/" + name + ".yml");
            if (!file.exists()) {
                file.createNewFile();
                BufferedWriter bw = new BufferedWriter(new FileWriter("plugins/DTP/panels/" + name + ".yml"));
                bw.write(result);
                bw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void send(String name, TextChannel channel, Member m, Guild g) {
        channel.sendMessage(BetterMessage.message("plugins/DTP/panels/" + name + ".yml", "panel", m, g))
                .addEmbeds(BetterEmbed.sendEmbed("plugins/DTP/panels/" + name + ".yml", "panel", m, g).build())
                .addActionRow(Button.primary("DTPpanel-" + name, "\uD83D\uDCE9")).queue();
    }
}
