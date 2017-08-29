/*
 * Copyright (c) 2017 Intland Software (support@intland.com)
 *
 */
package com.intland.codebeamer.api.client;

import org.apache.commons.lang3.ArrayUtils;

import java.io.File;

public class XUnitFileCollector {

    public void listFiles(File[] files) {
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                System.out.println("File " + files[i].getName());
            } else if (files[i].isDirectory()) {
                System.out.println("Directory " + files[i].getName());
            }
        }
    }

    public File[] getListOfFilesForDirectory(File directory) {
        return directory.listFiles();
    }

    public File[] getListOfFilesForMultipleDirectories(File[] directories) {
        File[] allFiles = new File[0];
        for (File directory : directories) {
            allFiles = ArrayUtils.addAll(allFiles, getListOfFilesForDirectory(directory));
        }
        return allFiles;
    }
}
