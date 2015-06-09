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

package com.fragstealers.log4j.core;

import com.fragstealers.log4j.func.LoggerConfigFunction;
import com.fragstealers.log4j.xml.binding.Appender;
import com.fragstealers.log4j.xml.binding.Logger;
import com.fragstealers.log4j.xml.binding.Root;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedMap;

import java.util.Map;
import java.util.Properties;

import static com.fragstealers.log4j.func.AppenderFunction.appenderFunction;
import static com.fragstealers.log4j.func.LoggerConfigFunction.loggerConfigFunction;
import static com.fragstealers.log4j.func.LoggerFunction.loggerFunction;
import static com.fragstealers.log4j.func.StartsWithPredicate.startsWith;
import static com.google.common.base.Predicates.containsPattern;
import static com.google.common.collect.Maps.filterKeys;

/**
 * Created by jason on 2/06/2015.
 */
public class Log4JModel {

    private final ImmutableSortedMap<String, String> log4jProperties;

    private ImmutableList<Appender> appenders;
    private ImmutableList<Logger> loggers;
    private Root root;

    public Log4JModel(Properties log4jProperties) {
        ImmutableSortedMap.Builder<String, String> builder = ImmutableSortedMap.orderedBy(String.CASE_INSENSITIVE_ORDER);

        for (String name : log4jProperties.stringPropertyNames()) {
            builder.put(name.trim().replaceFirst("^log4j\\.category\\.", "log4j.logger."), log4jProperties.getProperty(name));
        }

        this.log4jProperties = builder.build();
    }

    public ImmutableList<Appender> getAppenders() {
        if (appenders == null) {
            final Map<String, String> appenderEntries = filterKeys(log4jProperties,
                    containsPattern("^log4j\\.appender\\.[^\\.]+\\s*$"));

            appenders = FluentIterable.from(appenderEntries.entrySet())
                    .transform(appenderFunction(log4jProperties))
                    .toList();
        }
        return appenders;
    }

    public ImmutableList<Logger> getLoggers() {
        if (loggers == null) {
            final Map<String, String> loggerEntries = filterKeys(log4jProperties,
                    startsWith("log4j.logger."));

            loggers = FluentIterable.from(loggerEntries.entrySet())
                    .transform(loggerFunction(log4jProperties, getAppenders()))
                    .toList();
        }
        return loggers;
    }

    public Root getRootLogger() {
        if (root == null) {
            root = new Root();
            LoggerConfigFunction.LoggerConfig config = loggerConfigFunction(getAppenders())
                    .apply(log4jProperties.get("log4j.rootLogger"));

            root.getPriorityOrLevel().add(config.getLevel());
            root.getAppenderRef().addAll(config.getAppenderRef());

        }
        return root;
    }
}
