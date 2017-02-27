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

package com.fragstealers.log4j.ui.web
import com.fragstealers.log4j.core.Log4JPropertiesConverter
import groovy.json.internal.Charsets
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

import java.nio.charset.Charset
import java.util.logging.Logger

@Controller
class Log4JConverterServiceController {
    private static final Logger LOGGER = Logger.getLogger("com.fragstealers.log4j.ui.web.Log4JConverterServiceController");
    final Log4JPropertiesConverter log4JPropertiesConverter

    @Autowired
    def Log4JConverterServiceController(log4JPropertiesConverter) {
        this.log4JPropertiesConverter = log4JPropertiesConverter;
    }

    @RequestMapping(value = "/convert", produces = ["application/xml; charset=utf-8", "text/plain; charset=utf-8"])
    @ResponseBody
    def convert(InputStream propsStream) {
        LOGGER.info "Preparing to process a Log4J Properties file"
        def props = new Properties()
        props.load(new InputStreamReader(propsStream, Charsets.UTF_8))

        def dest = new StringWriter()
        log4JPropertiesConverter.toXml(props, dest)
        def xmlContent = dest.toString()

        LOGGER.info "XML file was generated"
        LOGGER.fine "Contents of the XML file: ${xmlContent}"
        return ResponseEntity.ok()
                .contentType(new MediaType(MediaType.APPLICATION_XML, Charset.forName("UTF-8")))
                .body(xmlContent);
    }
}
