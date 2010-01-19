//
// Generated from archetype; please customize.
//

package com.fragstealers.log4j

import generated.ObjectFactory

import generated.AppenderRef
import org.apache.log4j.ConsoleAppender
import org.apache.log4j.PatternLayout
import javax.xml.bind.JAXBContext
import generated.Param
import javax.xml.bind.Marshaller
import generated.Level

import com.fragstealers.log4j.jaxb.Log4JProperties

/**
 * Example Groovy class.
 */
class Example {
    def show() {
        println 'Hello World'
    }

    def produceLog4jConfig() {
        ObjectFactory objectFactory = new ObjectFactory()

        def layout = objectFactory.createLayout()
        layout.setClazz(PatternLayout.class.getName())
        layout.getParam().add(new Param(name: "Pattern", value: "%d{ABSOLUTE} %5p %c{1}:%L - %m%n"))

        def configuration = objectFactory.createLog4JConfiguration()
        configuration.setXmlnsLog4J("http://jakarta.apache.org/log4j/")

        def consoleAppender = objectFactory.createAppender()
        consoleAppender.setClazz(ConsoleAppender.class.name)
        consoleAppender.setName("CONSOLE")
        consoleAppender.setLayout(layout)

        def root = objectFactory.createRoot();
        root.getPriorityOrLevel().add(new Level(value: "DEBUG"))
        root.getAppenderRef().add(new AppenderRef(ref: consoleAppender))

        configuration.setRoot(root)
        configuration.getAppender().add(consoleAppender)

        def jc = JAXBContext.newInstance("generated");
        def marshaller = jc.createMarshaller()
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty("com.sun.xml.internal.bind.xmlHeaders", "\n<!DOCTYPE log4j:configuration SYSTEM \"log4j.dtd\">\n")
        marshaller.marshal(configuration, System.out)
    }

    def produceLog4jConfig3() {
        ObjectFactory objectFactory = new ObjectFactory()
        def log4JProperties = new Log4JProperties()

        def props = new Properties()
        props.load(new FileInputStream("/home/jroyals/work/code/other/log4j-converter/src/test/resources/log4j2.properties"))
        log4JProperties.fromProperties(props)

        def configuration = objectFactory.createLog4JConfiguration()
        configuration.setXmlnsLog4J("http://jakarta.apache.org/log4j/")



        configuration.setRoot(log4JProperties.rootLogger)
        configuration.getAppender().addAll(log4JProperties.getAppenders())
        configuration.getCategoryOrLogger().addAll(log4JProperties.getLoggers())
        configuration.setThreshold(props.getProperty("log4j.threshold"))

        def jc = JAXBContext.newInstance("generated");
        def marshaller = jc.createMarshaller()
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty("com.sun.xml.internal.bind.xmlHeaders", "\n<!DOCTYPE log4j:configuration SYSTEM \"log4j.dtd\">\n")
        marshaller.marshal(configuration, System.out)
    }

    public static void main(String[] args) {
        new Example().produceLog4jConfig3();
    }
}


