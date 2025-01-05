package xyz.volartrix.OGLG.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static xyz.volartrix.OGLG.Main.LOGGER;

public class CrashHandler {

    public static void crashHandler(Thread thread, Throwable throwable) {
        LOGGER.error("An error has occurred in thread: {}", thread.getName());
        LOGGER.error("See crash report below or in logs/crashes: {}", throwable.getMessage());

        String crashReport = CrashReportHandler.crashReportHandler(thread, throwable);
        LOGGER.error(crashReport);

        try {
            Files.createDirectories(Path.of("logs/crashes"));
            Files.write(Path.of("logs/crashes/crash-" + System.currentTimeMillis() + ".txt"), crashReport.getBytes(),
                    StandardOpenOption.CREATE);
        } catch (IOException e) {
            LOGGER.error("Failed to write crash report to file: {}", e.getMessage());
        }
    }

}
