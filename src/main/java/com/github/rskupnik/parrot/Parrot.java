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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Parrot {

    private final Map<String, String> properties = new HashMap<>();

    public static Parrot load(String... allowedFiles) {
        return new Parrot(allowedFiles);
    }

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
                        final String fileName = path.getFileName().toString();

                        if (fileName.endsWith(".properties") && isAllowed(path.toFile(), allowedFiles)) {
                            ingest(path.toFile());
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected Parrot() {
        // For unit tests
    }

    public Optional<String> get(String property) {
        return Optional.ofNullable(properties.get(property));
    }

    public Map<String, String> all() {
        return Collections.unmodifiableMap(properties);
    }

    private boolean isAllowed(File file, String[] allowedFiles) {
        if (allowedFiles == null || allowedFiles.length == 0)
            return true;

        for (String allowedFile : allowedFiles) {
            final String filename = allowedFile.contains(".properties") ? allowedFile.replace(".properties", "") : allowedFile;
            if (file.getName().replace(".properties", "").equals(filename))
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
            e.printStackTrace();
        }
    }

    private List<File> getFiles(String paths) {
        final List<File> filesList = new ArrayList<File>();
        for (String path : paths.split(File.pathSeparator)) {
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
        final File[] list = f.listFiles();
        if (list == null)
            return;

        for (File file : list) {
            if (file.isDirectory()) {
                recurse(filesList, file);
            } else {
                filesList.add(file);
            }
        }
    }
}
