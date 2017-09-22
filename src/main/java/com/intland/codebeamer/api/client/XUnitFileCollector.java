/*
 * Copyright (c) 2017 Intland Software (support@intland.com)
 *
 */
package com.intland.codebeamer.api.client;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class XUnitFileCollector {

    private static Logger logger;

    private Set<File> directories = new HashSet<>();

    public XUnitFileCollector() {
        logger = Logger.getLogger(XUnitFileCollector.class);
    }

    public XUnitFileCollector(Logger customLogger) {
        logger = customLogger;
    }

    public void addDirectory(File dir) {
        if (dir.isDirectory()) {
            directories.add(dir);
            for (File file: dir.listFiles()) {
                addDirectory(file);
            }
        }
    }

    public void listFiles(File[] files) {
        logger.info(getFileList(files));
    }

    public String getFileList(File[] files) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                builder.append(String.format("File %s%n", files[i].getName()));
            } else {
                builder.append(String.format("Directory %s%n", files[i].getName()));
                builder.append(getFileList(new File[]{files[i]}));
            }
        }
        return builder.toString().trim();
    }

    public File[] getFiles() {
        return getFiles(directories.toArray(new File[directories.size()]));
    }

    public File[] getFiles(File[] directories) {
        File[] allFiles = new File[0];
        for (File directory : directories) {
            allFiles = ArrayUtils.addAll(allFiles, getFiles(directory));
        }
        return allFiles;
    }

    public File[] getFiles(File directory) {
        try (Stream<File> fileStream = Arrays.stream(directory.listFiles())) {
            return fileStream.filter(file -> file.getName().endsWith("xml"))
                    .toArray(File[]::new);
        }
    }
}
