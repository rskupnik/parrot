/*
    Copyright 2016 Radosław Skupnik

    This file is part of Parrot.

    Parrot is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    Parrot is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Parrot; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package com.github.rskupnik.parrot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * When {@link #load(String...)} is called, Parrot will search for <i>properties</i> files in the classpath
 * and root directory (the directory that the application was ran from), parse them and store all properties in a single
 * internal {@code Map} - it will then return an instance of {@link Parrot} class containing all those properties.
 * <br><br>
 *
 * Having an instance of the {@link Parrot} class, properties can be accessed either using {@link #get(String)},
 * which returns an {@code Optional}, or {@link #all()} - which returns an <b>immutable</b> copy of the {@code Map}
 * that contains the properties.
 */
public class Parrot {

    private static final Logger LOGGER = LoggerFactory.getLogger(Parrot.class);

    private static final String PROPERTIES_EXTENSION = ".properties";

    private final Map<String, String> properties = new HashMap<>();

    /**
     * Search for <i>properties</i> files in classpath and root directory (the directory that the application was ran
     * from), parse them and store all properties in a single internal {@code Map}.
     * @param allowedFiles - a filter of file names to be loaded, if null - all files will be loaded
     * @return an instance of {@link Parrot} containing the loaded properties
     */
    public static Parrot load(String... allowedFiles) {
        return new Parrot(allowedFiles);
    }

    private Parrot(String... allowedFiles) {
        getFiles(System.getProperty("java.class.path"))
                .forEach(file -> {
                            if (file.getPath().endsWith(PROPERTIES_EXTENSION) && isAllowed(file, allowedFiles)) {
                                ingest(file);
                            }
                        }
                );

        try (Stream<Path> stream = Files.list(Paths.get(System.getProperty("user.dir")))){
                    stream.forEach(path -> {
                        final String fileName = path.getFileName().toString();

                        if (fileName.endsWith(PROPERTIES_EXTENSION) && isAllowed(path.toFile(), allowedFiles)) {
                            ingest(path.toFile());
                        }
                    });
        } catch (IOException e) {
            LOGGER.error("Error while trying to access files under user directory.", e);
        }
    }

    protected Parrot() {
        // For unit tests
    }

    /**
     * Retrieve a property, represented as an {@code Optional}
     *
     * @param property - name of the property to retrieve
     * @return an {@code Optional} containing the property if it exists
     */
    public Optional<String> get(String property) {
        return Optional.ofNullable(properties.get(property));
    }

    /**
     * @return an <b>immutable</b> {@code Map} copy containing all the properties.
     */
    public Map<String, String> all() {
        return Collections.unmodifiableMap(properties);
    }

    private boolean isAllowed(File file, String[] allowedFiles) {
        if (allowedFiles == null || allowedFiles.length == 0)
            return true;

        for (String allowedFile : allowedFiles) {
            final String filename = allowedFile.contains(PROPERTIES_EXTENSION) ? allowedFile.replace(PROPERTIES_EXTENSION, "") : allowedFile;
            if (file.getName().replace(PROPERTIES_EXTENSION, "").equals(filename))
                return true;
        }

        return false;
    }

    private void ingest(File file) {
        try {
            final Properties prop = new Properties();
            prop.load(new FileInputStream(file));
            prop.forEach((key, value) -> properties.put((String) key, (String) value));
        } catch (IOException e) {
            LOGGER.error(String.format("Error while trying to parse a .properties file: %s", file.getName()), e);
        }
    }

    private List<File> getFiles(String paths) {
        final List<File> filesList = new ArrayList<>();
        for (String path : paths.split(File.pathSeparator)) {
            final File file = new File(path);
            if (file.isDirectory()) {
                addFilesInDirectoryToList(filesList, file);
            } else {
                filesList.add(file);
            }
        }
        return filesList;
    }

    private void addFilesInDirectoryToList(List<File> filesList, File f) {
        final File[] list = f.listFiles();
        if (list == null)
            return;

        for (File file : list) {
            if (file.isDirectory()) {
                addFilesInDirectoryToList(filesList, file);
            } else {
                filesList.add(file);
            }
        }
    }
}
