package net.kynon.divonixtp.messages;

import net.kynon.divonixtp.Main;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class MsgFile {

    public static void loadMsg() throws FileNotFoundException {
        String filename = "messages.yml";
        ClassLoader classLoader = Main.class.getClassLoader();

        try (InputStream inputStream = classLoader.getResourceAsStream(filename)) {
            String result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

            File file = new File("plugins/DTP/" + filename);
            if (!file.exists()) {
                file.createNewFile();
                BufferedWriter bw = new BufferedWriter(new FileWriter("plugins/DTP/" + filename));
                bw.write(result);
                bw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
