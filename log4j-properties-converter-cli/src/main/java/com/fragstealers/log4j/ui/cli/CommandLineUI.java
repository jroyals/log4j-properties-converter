/*
 * Copyright (C) 2010 Jason Royals <log4j-converter@fragstealers.com>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 */

package com.fragstealers.log4j.ui.cli;

import com.fragstealers.log4j.core.Log4JPropertiesConverter;
import com.fragstealers.log4j.jaxb.JaxbLog4JPropertiesConverter;
import org.apache.commons.cli.*;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Properties;

public class CommandLineUI {

    private static final Options OPTIONS = new Options()
            .addOption(Option.builder("?").longOpt("help").desc("Show usage information and quit.").build())
            .addOption(Option.builder("file").required().hasArg().desc("Source log4j.properties file (required).").build())
            .addOption(Option.builder("v").longOpt("verbose").desc("Prints information to the screen.").build())
            .addOption(Option.builder("e").longOpt("encoding").hasArg().desc("Encoding of the properties file. Defaults to UTF-8.").build());

    public static void main(String[] args) {
        new CommandLineUI().showUi(args);
    }

    private void showUi(String[] args) {
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(OPTIONS, args);
            if (cmd.hasOption('?')) {
                printUsage();
            }
            File file = new File(cmd.getOptionValue("file"));
            Charset charset = Charset.forName(cmd.getOptionValue('e', "UTF-8"));
            boolean verbose = cmd.hasOption('v');
            doConvert(file, charset, verbose);
        } catch (ParseException e) {
            System.out.println("\nInvalid command line args.");
            printUsage();
        } catch (UnsupportedCharsetException e) {
            System.out.println("\nUnsupported charset value.");
            printUsage();
        }
    }

    //
    private void doConvert(File source, Charset charset, boolean isVerbose) {
        Properties log4jProperties = loadLog4jProperties(source, charset);

        String destFileName = source.getName().replaceAll("\\.properties", ".xml");
        File dest = new File(source.getAbsoluteFile().getParentFile(), destFileName);

        if (isVerbose) {
            System.out.println("Using source file " + source.getAbsolutePath() + " and creating new XML file at " + dest.getAbsolutePath());
        }

        final Log4JPropertiesConverter converter = new JaxbLog4JPropertiesConverter();

        try (Writer writer = new OutputStreamWriter(new FileOutputStream(dest), StandardCharsets.UTF_8)) {
            converter.toXml(log4jProperties, writer);
        } catch (IOException e) {
            System.out.println("Could not write file to destination: " + e.getMessage());
            printUsage();
        }

        System.out.println("Complete, wrote 1 file to " + dest.getParent());
    }

    private Properties loadLog4jProperties(File source, Charset charset) {
        Properties log4jProperties = new Properties();
        try {
            log4jProperties.load(new InputStreamReader(new FileInputStream(source), charset));
        } catch (Exception e) {
            System.out.println("\nUnable to parse properties file " + source.getAbsolutePath() + ", error was " + e.getMessage());
            printUsage();
        }
        return log4jProperties;
    }

    private void printUsage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("log4j-converter", OPTIONS);
    }
}
