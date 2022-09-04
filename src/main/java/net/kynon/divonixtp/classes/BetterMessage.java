package net.kynon.divonixtp.classes;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.kynon.divonixtp.Main;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Objects;

public class BetterMessage {

    public static String message(String message, Member member, Guild guild) {

        String msg = "";

        try {
            Yaml yaml = new Yaml();

            Map<String, Object> msgData = Main.replacePlaceholders(yaml.load(new FileInputStream("plugins/DTP/wpconfig.yml")), member, guild);

            if (!Objects.equals(msgData.get(message + ".message"), null)) {
                msg = msgData.get(message + ".message").toString();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return msg;
    }
}
