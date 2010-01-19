package com.fragstealers.log4j.jaxb.builder

import generated.Param
import generated.ObjectFactory

class LayoutBuilder {
    ObjectFactory objectFactory
    
    def buildFrom(String appenderName, Properties props) {
        def layout = objectFactory.createLayout()
        layout.setClazz(props.getProperty("log4j.appender.${appenderName}.layout"))

        props.each {String key, String value ->
            if (key.startsWith("log4j.appender.${appenderName}.layout.")) {
                BuilderUtils.addParam(layout, key, value)
            }
        }

        return layout
    }
}
