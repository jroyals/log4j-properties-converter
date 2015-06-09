package com.fragstealers.log4j.jaxb;

import com.fragstealers.log4j.core.Log4JModel;
import com.fragstealers.log4j.core.Log4JPropertiesConverter;
import com.fragstealers.log4j.xml.binding.Log4JConfiguration;
import com.fragstealers.log4j.xml.binding.ObjectFactory;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Properties;

public class JaxbLog4JPropertiesConverter implements Log4JPropertiesConverter {

    public static final ObjectFactory OBJECT_FACTORY = new ObjectFactory();

    public void toXml(Properties log4jProps, Writer writer) {
        final Log4JConfiguration configuration = OBJECT_FACTORY.createLog4JConfiguration();

        Log4JModel model = new Log4JModel(log4jProps);

        configuration.setXmlnsLog4J("http://jakarta.apache.org/log4j/");
        configuration.setThreshold(StringUtils.lowerCase(log4jProps.getProperty("log4j.threshold")));
        configuration.setDebug(log4jProps.getProperty("log4j.debug"));

        configuration.getAppender().addAll(model.getAppenders());
        configuration.setRoot(model.getRootLogger());
        configuration.getCategoryOrLogger().addAll(model.getLoggers());

        PrintWriter pw = new PrintWriter(writer);

        pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        pw.println("<!DOCTYPE log4j:configuration SYSTEM \"log4j.dtd\">");

        writeXmlUsingJaxB(configuration, pw);
        
    }


    private void writeXmlUsingJaxB(Log4JConfiguration configuration, Writer destination) {
        try {
            JAXBContext jc = JAXBContext.newInstance("com.fragstealers.log4j.xml.binding");
            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            marshaller.marshal(configuration, destination);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

}
