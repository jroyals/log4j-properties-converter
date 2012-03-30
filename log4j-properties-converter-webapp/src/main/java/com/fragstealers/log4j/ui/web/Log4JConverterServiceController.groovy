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
import java.util.logging.Logger
import org.apache.commons.io.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/convert")
class Log4JConverterServiceController {
    private static final Logger LOGGER = Logger.getLogger("com.fragstealers.log4j.ui.web.Log4JConverterServiceController");
    private static final HttpHeaders RESPONSE_HEADERS = new HttpHeaders(contentType: MediaType.APPLICATION_XML)
    final Log4JPropertiesConverter log4JPropertiesConverter

    @Autowired
    def Log4JConverterServiceController(log4JPropertiesConverter) {
        this.log4JPropertiesConverter = log4JPropertiesConverter;
    }

    @RequestMapping
    def convert(@RequestParam("props") String propsAsText) {
        LOGGER.info "Preparing to process a Log4J Properties file"
        LOGGER.fine "Contents of the file: ${propsAsText}"
        def props = new Properties()
        props.load(IOUtils.toInputStream(propsAsText))

        def dest = new StringWriter()
        log4JPropertiesConverter.toXml(props, dest)
        def xmlContent = dest.toString()

        LOGGER.info "XML file was generated"
        LOGGER.fine "Contents of the XML file: ${xmlContent}"
        return new ResponseEntity<String>(xmlContent, RESPONSE_HEADERS, HttpStatus.OK);
    }
}
