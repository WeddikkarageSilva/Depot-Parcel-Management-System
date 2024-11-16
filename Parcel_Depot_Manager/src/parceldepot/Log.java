package parceldepot;

import java.io.FileWriter;
import java.io.IOException;

public class Log {
    private static Log instance;
    private String logFilePath = "depot_log.txt";

    private Log() {}

    public static Log getInstance() {
        if (instance == null) {
            instance = new Log();
        }
        return instance;
    }

    public void logEvent(String event) {
        try (FileWriter writer = new FileWriter(logFilePath, true)) {
            writer.write(event + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
