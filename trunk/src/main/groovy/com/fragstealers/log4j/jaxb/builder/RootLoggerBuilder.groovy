package com.fragstealers.log4j.jaxb.builder

import com.fragstealers.log4j.xml.binding.AppenderRef
import com.fragstealers.log4j.xml.binding.Level
import com.fragstealers.log4j.xml.binding.ObjectFactory

class RootLoggerBuilder {

    private ObjectFactory objectFactory;

    def RootLoggerBuilder(def objectFactory) {
        this.objectFactory = objectFactory;
    }

    def buildFrom(Properties props, Map appenders) {
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

    private String getRootLoggerConfig(Properties props) {
        def rootConfigString = props.getProperty("log4j.rootLogger")
        if (rootConfigString == null) {
            rootConfigString = props.getProperty("log4j.rootCategory")
        }
        return rootConfigString
    }
}
