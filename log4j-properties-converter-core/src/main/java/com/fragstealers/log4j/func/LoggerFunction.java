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

package com.fragstealers.log4j.func;

import com.fragstealers.log4j.xml.binding.Appender;
import com.fragstealers.log4j.xml.binding.Logger;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedMap;

import java.util.Map;

import static com.fragstealers.log4j.func.LoggerConfigFunction.loggerConfigFunction;

public class LoggerFunction implements Function<Map.Entry<String, String>, Logger> {
    private final ImmutableSortedMap<String, String> log4jProperties;
    private final ImmutableList<Appender> appenders;

    private LoggerFunction(ImmutableSortedMap<String, String> log4jProperties, ImmutableList<Appender> appenders) {
        this.log4jProperties = log4jProperties;
        this.appenders = appenders;
    }

    public static LoggerFunction loggerFunction(ImmutableSortedMap<String, String> log4jProperties, ImmutableList<Appender> appenders) {
        return new LoggerFunction(log4jProperties, appenders);
    }

    @Override
    public Logger apply(Map.Entry<String, String> input) {
        Logger logger = new Logger();


        LoggerConfigFunction.LoggerConfig config = loggerConfigFunction(appenders).apply(input.getValue());

        logger.setName(input.getKey().replace("log4j.logger.", ""));
        logger.setAdditivity(log4jProperties.get("log4j.additivity." + logger.getName()));
        logger.setLevel(config.getLevel());
        logger.getAppenderRef().addAll(config.getAppenderRef());

        return logger;
    }
}
