package com.github.rskupnik.parrot;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Parrot {

    private static Map<String, String> properties = new HashMap<String, String>();

    private Parrot() {}

    public static void init() throws IOException {
        getFiles(System.getProperty("java.class.path"))
                .forEach(file -> {
                    if (file.getPath().endsWith(".properties")) {
                        ingest(file);
                    }
                }
        );

        try {
            Files.list(Paths.get(System.getProperty("user.dir")))
                    .forEach(path -> {
                        String fileName = path.getFileName().toString();

                        if (fileName.endsWith(".properties")) {
                            ingest(path.toFile());
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Optional<String> get(String property) {
        String value = properties.get(property);
        return value != null ? Optional.of(value) : Optional.empty();
    }

    public static Map<String, String> all() {
        return new HashMap<>(properties);
    }

    private static void ingest(File file) {
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream(file));
            prop.entrySet().forEach(entry -> {
                properties.put((String) entry.getKey(), (String) entry.getValue());
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<File> getFiles(String paths) {
        List<File> filesList = new ArrayList<File>();
        for (final String path : paths.split(File.pathSeparator)) {
            final File file = new File(path);
            if( file.isDirectory()) {
                recurse(filesList, file);
            }
            else {
                filesList.add(file);
            }
        }
        return filesList;
    }

    private static void recurse(List<File> filesList, File f) {
        File list[] = f.listFiles();
        for (File file : list) {
            if (file.isDirectory()) {
                recurse(filesList, file);
            }
            else {
                filesList.add(file);
            }
        }
    }
}
