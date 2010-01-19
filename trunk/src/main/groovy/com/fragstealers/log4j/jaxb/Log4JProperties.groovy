package com.fragstealers.log4j.jaxb

import generated.ObjectFactory
import com.fragstealers.log4j.jaxb.builder.AppenderBuilder
import com.fragstealers.log4j.jaxb.builder.RootLoggerBuilder
import com.fragstealers.log4j.jaxb.builder.LoggerBuilder
import generated.Logger

class Log4JProperties {
    private static final KNOWN_LEVELS = ["OFF", "FATAL", "ERROR", "WARN", "INFO", "DEBUG", "ALL"]

    private def appenderMap = [:]
    private def loggers = []
    private def rootLogger

    private ObjectFactory objectFactory
    private AppenderBuilder appenderBuilder;
    private RootLoggerBuilder rootLoggerBuilder;
    private LoggerBuilder loggerBuilder;

    def Log4JProperties() {
        objectFactory = new ObjectFactory();
        appenderBuilder = new AppenderBuilder(objectFactory)
        rootLoggerBuilder = new RootLoggerBuilder(objectFactory)
        loggerBuilder = new LoggerBuilder(objectFactory)
    }

    def fromProperties(Properties log4jProperties) {
        log4jProperties.each {String key, String value ->
            if (key ==~ /log4j\.appender\.\w+\s*/) {
                String name = key.tokenize(".").last()
                appenderMap.put(name, appenderBuilder.buildFrom(name, value, log4jProperties))
            }
        }

        log4jProperties.each {String key, String value ->
            if (key ==~ /log4j\.logger\..*/ || key ==~ /log4j\.category\..*/) {
                loggers.add(loggerBuilder.buildFrom(key, value, appenderMap, log4jProperties))
            }
        }

        rootLogger = rootLoggerBuilder.buildFrom(log4jProperties, appenderMap)
    }

    def getAppenders() {
        return appenderMap.values()
    }

    def getLoggers() {
        return loggers
    }

    def getRootLogger() {
        return rootLogger;
    }
}
