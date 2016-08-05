package edu.pk.core.util;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.Date;

public class FolderWatcher {
    public static void main(String[] args) {
        Logger logger = new Logger();

        Path path = FileSystems.getDefault().getPath("/home/prabhat/Downloads");
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
            boolean flag = true;
            logger.log("Path", "Action", "Date");
            logger.log("-------------------------------------------------------------------------------------", "----------------", "----------------");
            while (flag) {
                WatchKey watchKey = watchService.take();
                watchKey.pollEvents().forEach(watchEvent -> {
                    Object context = watchEvent.context();
                    if (context instanceof Path) {
                        Path alteredPath = ((Path) context);
                        long lastModified = System.currentTimeMillis();
                        logger.log(alteredPath.toAbsolutePath().toString(),
                                watchEvent.kind().name(), new Date(lastModified).toString());
                    }
                });
                watchKey.reset();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.close();
    }

    private static class Logger {
        private FileWriter fileWriter;

        private Logger() {
            try {
                fileWriter = new FileWriter("/home/prabhat/Documents/dir-log.txt");
            } catch (IOException e) {
                System.err.println(e);
            }
        }

        private void log(String path, String action, String date) {
            try {
                fileWriter.write(String.format("\n%-100s%-20s%-20s", path, action, date));
                fileWriter.flush();
            } catch (IOException e) {
                System.err.println(e);
            }
        }

        private void close() {
            try {
                fileWriter.close();
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }
}
