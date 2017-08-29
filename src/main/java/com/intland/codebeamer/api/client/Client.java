/*
 * Copyright (c) 2017 Intland Software (support@intland.com)
 *
 */
package com.intland.codebeamer.api.client;

import org.apache.commons.cli.*;
import org.apache.log4j.Logger;

import java.nio.file.Paths;

public class Client {
    private static Logger logger = Logger.getLogger(Client.class);

    private Client() {
    }

    public static void main(String[] args) throws Exception {
        Options options = new Options();
        Option pathOption = new Option("p", "path", true, "path of XUnit results");
        pathOption.setRequired(true);
        options.addOption(pathOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException ex) {
            logger.warn(ex.getMessage());
            formatter.printHelp("codebeamer-api-client", options);

            System.exit(1);
            return;
        }

        String inputPath = cmd.getOptionValue("path");

        XUnitFileCollector collector = new XUnitFileCollector();
        collector.listFiles(collector.getListOfFilesForDirectory(Paths.get(inputPath).toFile()));
    }
}
