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

package com.fragstealers.log4j;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Properties;
import com.fragstealers.log4j.jaxb.JaxbLog4JPropertiesConverter;
import org.apache.commons.io.IOUtils;
import org.custommonkey.xmlunit.Validator;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ConvertFromPropertiesToXmlTest {

    private JaxbLog4JPropertiesConverter converter = new JaxbLog4JPropertiesConverter();

    @Test
    public void canConvertPropertiesToXmlFormat() throws IOException, SAXException {
        StringWriter writer = new StringWriter();
        Properties log4jProperties = new Properties();
        log4jProperties.load(getClass().getClassLoader().getResourceAsStream("log4j.issue4.properties"));

        converter.toXml(log4jProperties, writer);

        String expected = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("log4j.issue4.expected.xml"));
        String actual = writer.toString();

        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setControlEntityResolver(new Log4JEntityResolver());
        XMLUnit.setTestEntityResolver(new Log4JEntityResolver());

        Validator validator = new Validator(actual);
        validator.assertIsValid();
        
        XMLAssert.assertXMLEqual(expected, actual);
    }
    
    @Test
    public void ignoresTrailingWhitespace() throws Exception {
        StringWriter writer = new StringWriter();
        Properties log4jProperties = new Properties();
        log4jProperties.load(getClass().getClassLoader().getResourceAsStream("log4j.issue5.properties"));

        converter.toXml(log4jProperties, writer);

        String expected = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("log4j.issue5.expected.xml"));
        String actual = writer.toString();

        System.out.println(expected);
        System.out.println("=============================");
        System.out.println(actual);
        
        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setControlEntityResolver(new Log4JEntityResolver());
        XMLUnit.setTestEntityResolver(new Log4JEntityResolver());

        Validator validator = new Validator(actual);
        validator.assertIsValid();
        
        XMLAssert.assertXMLEqual(expected, actual);    	
    }

    private static class Log4JEntityResolver implements EntityResolver {
        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
            if (systemId != null && systemId.endsWith("log4j.dtd")) {
                return new InputSource(getClass().getClassLoader().getResourceAsStream("org/apache/log4j/xml/log4j.dtd"));
            }
            return null;
        }
    }
}
