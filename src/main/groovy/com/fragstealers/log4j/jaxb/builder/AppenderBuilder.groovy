package com.fragstealers.log4j.jaxb.builder

import com.fragstealers.log4j.xml.binding.ObjectFactory

class  AppenderBuilder {

    ObjectFactory objectFactory
    private LayoutBuilder layoutBuilder;

    def AppenderBuilder(def objectFactory) {
        this.objectFactory = objectFactory
        layoutBuilder = new LayoutBuilder(objectFactory: objectFactory)
    }

    def buildFrom(String name, String value, Properties props) {
        def appender = objectFactory.createAppender()
        appender.setName(name)
        appender.setClazz(props.getProperty("log4j.appender.${name}"))

        if (props.containsKey("log4j.appender.${name}.layout".toString())) {
            def layout = layoutBuilder.buildFrom(name, props)
            appender.setLayout(layout)
        }

        props.each {String key, String optionValue ->
            if (key.startsWith("log4j.appender.${name}.") && !key.contains("layout")) {
                BuilderUtils.addParam(appender, key, optionValue)
            }
        }

        return appender
    }
}
