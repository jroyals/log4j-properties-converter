package com.fragstealers.log4j.ui

import com.fragstealers.log4j.jaxb.JaxbLog4JPropertiesConverter

class CommandLineUI {
    def showUi(def args) {
        def cl = new CliBuilder(usage: 'log4j-converter -f "path" [-h] [-v] [-o "dir"] <path to log4j.properties> []')

        cl.h(longOpt: 'help', 'Show usage information and quit')
        cl.f(argName: 'file', longOpt: 'file', args: 1, required: true, 'Source log4j.properties file')
        cl.o(argName: 'out', longOpt: 'output-directory', args: 1, required: false, 'Output directory where new file will be written. Default is the same directory as the source file.')
        cl.v(argName: 'verbose', longOpt: 'verbose', args: 1, required: false, 'Prints information to the screen.')


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
                def log4jProperties = new Properties()
                try {
                    log4jProperties.load(source.newInputStream())
                } catch (Exception e) {
                    println "\nUnable to parse properties file ${opt.f}, error was ${e.message}"
                    cl.usage()
                }

                // -o option should override
                def destFileName = source.name.replaceAll(/.properties/, ".xml")
                def dest = new File(source.parent, destFileName)

                if (opt.v) {
                    println "Using source file ${source.absolutePath} and creating new XML file at ${dest.absolutePath}"
                }

                def converter = new JaxbLog4JPropertiesConverter()
                dest.withWriter {writer ->
                    converter.toXml(log4jProperties, writer)
                }

                println "Complete, wrote 1 file to ${dest.parent}"
            }
        }
    }

    public static void main(String[] args) {
        new CommandLineUI().showUi(args)
    }
}