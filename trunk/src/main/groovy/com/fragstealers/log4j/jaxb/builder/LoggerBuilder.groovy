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

package com.fragstealers.log4j.jaxb.builder

import com.fragstealers.log4j.xml.binding.AppenderRef
import com.fragstealers.log4j.xml.binding.Level
import com.fragstealers.log4j.xml.binding.Logger
import com.fragstealers.log4j.xml.binding.ObjectFactory

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
