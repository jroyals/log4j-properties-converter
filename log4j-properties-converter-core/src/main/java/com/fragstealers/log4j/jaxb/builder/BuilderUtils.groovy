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

package com.fragstealers.log4j.jaxb.builder

import com.fragstealers.log4j.xml.binding.Param
import java.util.logging.Logger

class BuilderUtils {
    private static final Logger log = Logger.getLogger("com.fragstealers.log4j.jaxb.builder.BuilderUtils");
    
    static levels = ["OFF", "FATAL", "ERROR", "WARN", "INFO", "DEBUG", "ALL", "INHERIT", "NULL", "TRACE"]
    
    static void addParam(def objectToAdd, String fullName, String value) {
        String name = fullName.tokenize(".").last()
        objectToAdd.getParam().add(new Param(name: name, value: value))
    }

    static boolean isValidLoggingLevel(String level) {
        def levelToFind = level?.trim()?.toUpperCase()
        def result = levels.contains(levelToFind)
        log.fine("Level '$levelToFind' is a valid Log4J Level? '$result'")
        return result;
    }
}
