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

import com.fragstealers.log4j.xml.binding.ObjectFactory

class LayoutBuilder {
    ObjectFactory objectFactory
    
    def buildFrom(String appenderName, Map props) {
        def layout = objectFactory.createLayout()
        layout.setClazz(props.get("log4j.appender.${appenderName}.layout"))

        props.each {String key, String value ->
            if (key.startsWith("log4j.appender.${appenderName}.layout.")) {
                BuilderUtils.addParam(layout, key, value)
            }
        }

        return layout
    }
}