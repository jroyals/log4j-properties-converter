package com.fragstealers.log4j.core;

import java.io.Writer;
import java.util.Properties;

public interface Log4JPropertiesConverter {
    void toXml(Properties properties, Writer destination);
}
