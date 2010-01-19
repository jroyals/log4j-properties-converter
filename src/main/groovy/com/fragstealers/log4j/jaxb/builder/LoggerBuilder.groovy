package com.fragstealers.log4j.jaxb.builder

import com.fragstealers.log4j.xml.binding.AppenderRef
import com.fragstealers.log4j.xml.binding.Level
import com.fragstealers.log4j.xml.binding.Logger
import com.fragstealers.log4j.xml.binding.ObjectFactory

////////////////////////////////////////////////////////////////////////////////
//
// Copyright (c) 2010, Suncorp Metway Limited. All rights reserved.
//
// This is unpublished proprietary source code of Suncorp Metway Limited.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
////////////////////////////////////////////////////////////////////////////////

class LoggerBuilder {
    private ObjectFactory objectFactory;

    def LoggerBuilder(def objectFactory) {
        this.objectFactory = objectFactory;
    }

    def buildFrom(String name, String value, Map appenders, Properties props) {
        def logger = objectFactory.createLogger()
        logger.setName(extractLoggerName(name))
        def configItems = value.tokenize(",").collect {it.trim()}

        // do we have a valid Level as the first arg? If so, set the level and then remove the element
        if (BuilderUtils.isValidLoggingLevel(configItems[0])) {
            logger.setLevel(new Level(value: configItems.remove(0)?.toLowerCase()))
        }

        configItems.each {appenderRef ->
            def appender = appenders.get(appenderRef)
            logger.getAppenderRef().add(new AppenderRef(ref: appender))
        }

        checkAdditivity(props, logger)

        return logger
    }

    private def checkAdditivity(Properties props, Logger logger) {
        def additivity = getAdditivityProperty(props, logger.getName())
        if (additivity != null) {
            logger.setAdditivity(additivity)
        }
    }

    private def extractLoggerName(String unprocessedName) {
        def loggersRemoved = unprocessedName.replaceAll(/log4j\.logger\./, "")
        return loggersRemoved.replaceAll(/log4j\.category\./, "")
    }

    private def getAdditivityProperty(Properties log4jProperties, String loggerName) {
        String additivityKey = "log4j.additivity.${loggerName}"

        String additivity = log4jProperties.getProperty(additivityKey);
        if (additivity != null) {
            return additivity;
        }
        return null;
    }
}
