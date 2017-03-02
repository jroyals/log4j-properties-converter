/*
 * Copyright (C) 2015 Jason Royals <log4j-converter@fragstealers.com>
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

package com.fragstealers.log4j.jaxb;

import com.fragstealers.log4j.core.Log4JModel;
import com.fragstealers.log4j.core.Log4JPropertiesConverter;
import com.fragstealers.log4j.xml.binding.Log4JConfiguration;
import com.fragstealers.log4j.xml.binding.ObjectFactory;

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
        configuration.setThreshold(lowerCase(log4jProps.getProperty("log4j.threshold")));
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

    private static String lowerCase(String str) {
        if (str == null) {
            return null;
        }
        return str.toLowerCase();
    }

}
