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
        marshaller.setProperty("com.sun.xml.internal.bind.xmlHeaders", "\n<!DOCTYPE log4j:configuration SYSTEM \"log4j.dtd\">\n")
        marshaller.marshal(configuration, destination)
    }
}
