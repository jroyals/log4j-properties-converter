package com.fragstealers.log4j.ui.web

import com.fragstealers.log4j.core.Log4JPropertiesConverter
import org.apache.commons.io.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam

/**
 * Created by IntelliJ IDEA.
 * User: jason
 * Date: 15/11/2010
 * Time: 10:59:30 AM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/convert")
class Log4JConverterServiceController {
  Log4JPropertiesConverter log4JPropertiesConverter

  @Autowired
  def Log4JConverterServiceController(log4JPropertiesConverter) {
    this.log4JPropertiesConverter = log4JPropertiesConverter;
  }

  @RequestMapping
  def convert(@RequestParam("props") String propsAsText) {
    def props = new Properties()
    props.load(IOUtils.toInputStream(propsAsText))

    def dest = new StringWriter()

    log4JPropertiesConverter.toXml(props, dest)
    HttpHeaders responseHeaders = new HttpHeaders()
    responseHeaders.contentType = MediaType.APPLICATION_XML
    return new ResponseEntity<String>(dest.toString(), responseHeaders, HttpStatus.OK);
  }
}
