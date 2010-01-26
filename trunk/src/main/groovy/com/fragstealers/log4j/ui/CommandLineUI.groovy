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

package com.fragstealers.log4j.ui

import com.fragstealers.log4j.jaxb.JaxbLog4JPropertiesConverter

class CommandLineUI {
    def showUi(def args) {
        def cl = new CliBuilder(usage: 'log4j-converter [-v] -f <path to log4j.properties>')

        cl.h(longOpt: 'help', 'Show usage information and quit')
        cl.f(argName: 'file', longOpt: 'file', args: 1, required: true, 'Source log4j.properties file')
        cl.v(argName: 'verbose', longOpt: 'verbose', required: false, 'Prints information to the screen.')

        def opt = cl.parse(args)

        if (!opt) {
            // because the parse failed, the usage will be shown automatically
            println "\nInvalid command line, exiting..."
        } else if (opt.h) {
            cl.usage()
        } else {
            def source = new File(opt.f)
            if (!source.isFile()) {
                println "\nUnable to open file ${opt.f}"
                cl.usage()
            } else {
                doConvert(source, cl, opt)
            }
        }
    }

    private def doConvert(def source, CliBuilder cl, OptionAccessor opt) {
        def log4jProperties = loadLog4jProperties(source, cl, opt)

        // -o option should override
        def destFileName = source.name.replaceAll(/.properties/, ".xml")
        def dest = new File(source.absoluteFile.parentFile, destFileName)

        if (opt.v) {
            println "Using source file ${source.absolutePath} and creating new XML file at ${dest.absolutePath}"
        }

        def converter = new JaxbLog4JPropertiesConverter()
        dest.withWriter {writer ->
            converter.toXml(log4jProperties, writer)
        }

        println "Complete, wrote 1 file to ${dest.parent}"
    }

    private def loadLog4jProperties(File source, CliBuilder cl, opt) {
        def log4jProperties = new Properties()
        try {
            log4jProperties.load(source.newInputStream())
        } catch (Exception e) {
            println "\nUnable to parse properties file ${opt.f}, error was ${e.message}"
            cl.usage()
        }
        return log4jProperties
    }

    public static void main(String[] args) {
        new CommandLineUI().showUi(args)
    }
}