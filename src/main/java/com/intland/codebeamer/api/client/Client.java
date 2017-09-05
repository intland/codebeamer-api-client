/*
 * Copyright (c) 2017 Intland Software (support@intland.com)
 *
 */
package com.intland.codebeamer.api.client;

import com.intland.codebeamer.api.client.rest.RestAdapter;
import com.intland.codebeamer.api.client.rest.RestAdapterImpl;
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

        Option cbAddressOption = new Option("u", "url", true, "url of codebeamer");
        cbAddressOption.setRequired(true);
        options.addOption(cbAddressOption);

        Option usernameOption = new Option("U", "username", true, "username for codebeamer");
        usernameOption.setRequired(true);
        options.addOption(usernameOption);

        Option passwordOption = new Option("P", "password", true, "password for codebeamer");
        passwordOption.setRequired(true);
        options.addOption(passwordOption);

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

        final String inputPath = cmd.getOptionValue("path");
        final String url = cmd.getOptionValue("url");
        final String username = cmd.getOptionValue("username");
        final String password = cmd.getOptionValue("password");

        Configuration config = new Configuration(url, username, password);
        RestAdapter rest = new RestAdapterImpl(config, null);

        logger.info(String.format("Target Codebeamer Version is %s", rest.getVersion()));

        XUnitFileCollector collector = new XUnitFileCollector();
        collector.listFiles(collector.getListOfFilesForDirectory(Paths.get(inputPath).toFile()));
    }
}
