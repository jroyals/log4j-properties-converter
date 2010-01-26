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

package com.fragstealers.log4j.jaxb

import com.fragstealers.log4j.core.Log4JPropertiesConverter
import com.fragstealers.log4j.jaxb.builder.AppenderBuilder
import com.fragstealers.log4j.jaxb.builder.LoggerBuilder
import com.fragstealers.log4j.jaxb.builder.RootLoggerBuilder
import com.fragstealers.log4j.xml.binding.ObjectFactory
import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller
import com.fragstealers.log4j.xml.binding.Log4JConfiguration

class JaxbLog4JPropertiesConverter implements Log4JPropertiesConverter {
    private static final KNOWN_LEVELS = ["OFF", "FATAL", "ERROR", "WARN", "INFO", "DEBUG", "ALL"]

    def void toXml(Properties log4jProperties, Writer destination) {
        def objectFactory = new ObjectFactory();
        def appenderBuilder = new AppenderBuilder(objectFactory)
        def rootLoggerBuilder = new RootLoggerBuilder(objectFactory)
        def loggerBuilder = new LoggerBuilder(objectFactory)

        def configuration = objectFactory.createLog4JConfiguration()

        processSimpleProperties(configuration, log4jProperties)
        def appenderMap = processAppenders(log4jProperties, appenderBuilder)
        def loggers = processLoggers(log4jProperties, loggerBuilder, appenderMap)
        def rootLogger = rootLoggerBuilder.buildFrom(log4jProperties, appenderMap)

        configuration.setRoot(rootLogger)
        configuration.getAppender().addAll(appenderMap.values())
        configuration.getCategoryOrLogger().addAll(loggers)

        writeXmlUsingJaxB(configuration, destination)
    }

    private def processSimpleProperties(Log4JConfiguration configuration, Properties log4jProperties) {
        configuration.setXmlnsLog4J "http://jakarta.apache.org/log4j/"
        configuration.setThreshold log4jProperties["log4j.threshold"]?.toLowerCase()
        configuration.setDebug log4jProperties["log4j.debug"]
    }

    private def processLoggers(Properties log4jProperties, LoggerBuilder loggerBuilder, Map appenderMap) {
        def loggers = []
        log4jProperties.each {String key, String value ->
            if (key ==~ /log4j\.logger\..*/ || key ==~ /log4j\.category\..*/) {
                def logger = loggerBuilder.buildFrom(key, value, appenderMap, log4jProperties)
                loggers.add(logger)
            }
        }
        return loggers
    }

    private def processAppenders(Properties log4jProperties, AppenderBuilder appenderBuilder) {
        def appenderMap = [:]
        log4jProperties.each {String key, String value ->
            if (key ==~ /log4j\.appender\.\w+\s*/) {
                String name = key.tokenize(".").last()
                def appender = appenderBuilder.buildFrom(name, value, log4jProperties)
                appenderMap.put(name, appender)
            }
        }
        return appenderMap
    }

    private def writeXmlUsingJaxB(Log4JConfiguration configuration, Writer destination) {
        def jc = JAXBContext.newInstance("com.fragstealers.log4j.xml.binding");
        def marshaller = jc.createMarshaller()
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty("com.sun.xml.bind.xmlHeaders", "\n<!DOCTYPE log4j:configuration SYSTEM \"log4j.dtd\">\n")
        marshaller.marshal(configuration, destination)
    }
}
