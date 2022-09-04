package net.kynon.divonixtp.classes;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.kynon.divonixtp.Main;
import org.yaml.snakeyaml.Yaml;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class BetterEmbed {

    public static EmbedBuilder sendEmbed(String message, Member member, Guild guild) {

        EmbedBuilder eb = new EmbedBuilder();

        try {
            Yaml yaml = new Yaml();

            Map<String, Object> msgData = Main.replacePlaceholders(yaml.load(new FileInputStream("plugins/DTP/wpconfig.yml")), member, guild);

            // EMBED BUILDING

            if (Boolean.parseBoolean(msgData.get(message + ".embed").toString())) {
                if (!Objects.equals(msgData.get(message + ".title"), null)) {
                    eb.setTitle(msgData.get(message + ".title").toString());
                }

                if (!Objects.equals(msgData.get(message + ".description"), null)) {
                    eb.setDescription(msgData.get(message + ".description").toString());
                }

                if (!Objects.equals(msgData.get(message + ".footer"), null)) {
                    eb.setFooter(msgData.get(message + ".footer").toString());
                }

                if (!Objects.equals(msgData.get(message + ".image"), null)) {
                    eb.setImage(msgData.get(message + ".image").toString());
                }

                if (!Objects.equals(msgData.get(message + ".thumbnail"), null) && !Objects.equals(msgData.get(message + ".thumbnail"), "")) {
                    eb.setThumbnail(msgData.get(message + ".thumbnail").toString() );
                }

                if (!Objects.equals(msgData.get(message + ".color"), null)) {
                    eb.setColor(new Color(Integer.parseInt(msgData.get(message + ".color").toString().split(",")[0]),
                            Integer.parseInt(msgData.get(message + ".color").toString().split(",")[1]),
                            Integer.parseInt(msgData.get(message + ".color").toString().split(",")[2])));
                }

                if (!Objects.equals(msgData.get(message + ".fields"), null)) {
                    for (String s : Arrays.asList(msgData.get(message + ".fields").toString())) {
                        String[] f = s.split("\\|~\\|");
                        eb.addField(f[0].substring(1), f[1], Boolean.parseBoolean(f[2].substring(0, f[2].length() - 1)));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return eb;
    }
}
