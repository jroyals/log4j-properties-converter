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
import com.fragstealers.log4j.xml.binding.ObjectFactory

class RootLoggerBuilder {

    private ObjectFactory objectFactory;

    def RootLoggerBuilder(def objectFactory) {
        this.objectFactory = objectFactory;
    }

    def buildFrom(Map props, Map appenders) {
        def rootLogger = null;

        String rootConfigString = getRootLoggerConfig(props)

        if (rootConfigString != null) {
            rootLogger = objectFactory.createRoot()

            def configItems = rootConfigString.tokenize(",").collect {it.trim()}

            // do we have a valid Level as the first arg? If so, set the level and then remove the element
            if (BuilderUtils.isValidLoggingLevel(configItems[0])) {
                rootLogger.getPriorityOrLevel().add(new Level(value: configItems.remove(0)?.toLowerCase()))
            }

            configItems.each {appenderRef ->
                def appender = appenders.get(appenderRef)
                rootLogger.getAppenderRef().add(new AppenderRef(ref: appender))
            }
        }
        return rootLogger
    }

    private String getRootLoggerConfig(Map props) {
        def rootConfigString = props.get("log4j.rootLogger")
        if (rootConfigString == null) {
            rootConfigString = props.get("log4j.rootCategory")
        }
        return rootConfigString
    }
}
