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

class  AppenderBuilder {

    ObjectFactory objectFactory
    private LayoutBuilder layoutBuilder;

    def AppenderBuilder(def objectFactory) {
        this.objectFactory = objectFactory
        layoutBuilder = new LayoutBuilder(objectFactory: objectFactory)
    }

    def buildFrom(String name, String value, Map props) {
        String appenderName = "log4j.appender.${name}"
        def appender = objectFactory.createAppender()
        appender.setName(name)
        appender.setClazz(props.get(appenderName))

        if (props.containsKey("log4j.appender.${name}.layout".toString())) {
            def layout = layoutBuilder.buildFrom(name, props)
            appender.setLayout(layout)
        }

        props.each {String key, String optionValue ->
            if (key.startsWith(appenderName + ".") && !key.contains("layout")) {
                BuilderUtils.addParam(appender, key, optionValue)
            }
        }

        return appender
    }
}
