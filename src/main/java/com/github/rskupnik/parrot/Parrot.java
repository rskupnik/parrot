package com.github.rskupnik.parrot;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Parrot {

    private static Parrot INSTANCE;
    private static Parrot MOCK;

    /**
     * Receives either the newest instance of Parrot that was created by calling
     * <b>newInstance()</b> or the mock instance, if <b>mock()</b> was used.
     *
     * @return the mock instance if mock() was used or the newest regular instance
     * created with newInstance()
     */
    public static Parrot getInstance() {
        if (MOCK != null)
            return MOCK;

        if (INSTANCE == null)
            newInstance();

        return INSTANCE;
    }

    /**
     * Sets a mock instance of Parrot.
     * <br/>
     * If set, this instance will be always returned by <b>getInstance()</b>
     * <br/>
     * You can use it to mock Parrot when using it in a single static context
     *
     * @param parrot an instance of Parrot to set the mock to
     */
    public static void mock(Parrot parrot) {
        MOCK = parrot;
    }

    /**
     * Creates a new instance of Parrot and saves it as the current instance.
     * <br/>
     * <br/>
     * The current instance can be withdrawn using <b>getInstance()</b>
     * (unless <b>mock()</b> was used - the mocked instance will be returned instead).
     * <br/>
     * <br/>
     * This allows creating multiple different instances of Parrot or using
     * it in a static context by just calling <b>Parrot.newInstance()</b> and
     * then <b>Parrot.getInstance()</b>.
     *
     * @param allowedFiles names of files that Parrot is allowed to load
     * @return a new instance of Parrot
     */
    public static Parrot newInstance(String... allowedFiles) {
        INSTANCE = new Parrot(allowedFiles);
        return INSTANCE;
    }

    private Map<String, String> properties = new HashMap<String, String>();

    private Parrot(String... allowedFiles) {
        getFiles(System.getProperty("java.class.path"))
                .forEach(file -> {
                            if (file.getPath().endsWith(".properties")) {
                                if (isAllowed(file, allowedFiles))
                                    ingest(file);
                            }
                        }
                );

        try {
            Files.list(Paths.get(System.getProperty("user.dir")))
                    .forEach(path -> {
                        String fileName = path.getFileName().toString();

                        if (fileName.endsWith(".properties") && isAllowed(path.toFile(), allowedFiles)) {
                            ingest(path.toFile());
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isAllowed(File file, String[] allowedFiles) {
        if (allowedFiles == null || allowedFiles.length == 0)
            return true;

        for (String allowedFile : allowedFiles) {
            String filename = allowedFile.contains(".properties") ? allowedFile.replace(".properties", "") : allowedFile;
            if (file.getName().replace(".properties", "").equals(filename))
                return true;
        }

        return false;
    }

    public Optional<String> get(String property) {
        String value = properties.get(property);
        return value != null ? Optional.of(value) : Optional.empty();
    }

    public Map<String, String> all() {
        return new HashMap<>(properties);
    }

    private void ingest(File file) {
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

    private List<File> getFiles(String paths) {
        List<File> filesList = new ArrayList<File>();
        for (final String path : paths.split(File.pathSeparator)) {
            final File file = new File(path);
            if (file.isDirectory()) {
                recurse(filesList, file);
            } else {
                filesList.add(file);
            }
        }
        return filesList;
    }

    private void recurse(List<File> filesList, File f) {
        File list[] = f.listFiles();
        for (File file : list) {
            if (file.isDirectory()) {
                recurse(filesList, file);
            } else {
                filesList.add(file);
            }
        }
    }
}
