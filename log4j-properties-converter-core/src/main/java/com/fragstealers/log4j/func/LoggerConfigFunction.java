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
import com.fragstealers.log4j.xml.binding.AppenderRef;
import com.fragstealers.log4j.xml.binding.Level;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class LoggerConfigFunction implements Function<String, LoggerConfigFunction.LoggerConfig> {
    private final ImmutableMap<String, Appender> appenders;

    private LoggerConfigFunction(ImmutableList<Appender> appenders) {
        final ImmutableMap.Builder<String, Appender> builder = ImmutableMap.builder();

        for (Appender appender : appenders) {
            builder.put(appender.getName(), appender);
        }

        this.appenders = builder.build();
    }

    public static LoggerConfigFunction loggerConfigFunction(ImmutableList<Appender> appenders) {
        return new LoggerConfigFunction(appenders);
    }

    @Override
    public LoggerConfig apply(String input) {
        final String[] tokens = input.split(",");
        final LoggerConfig loggerConfig = new LoggerConfig();
        if (tokens.length > 0) {
            loggerConfig.level = new Level();
            loggerConfig.level.setValue(tokens[0].trim().toUpperCase());
        }

        ImmutableList.Builder<AppenderRef> appenderRefs = ImmutableList.builder();
        for (int i = 1; i < tokens.length; i++) {
            final String value = tokens[i].trim();
            AppenderRef appenderRef = new AppenderRef();
            appenderRef.setRef(appenders.get(value));

            appenderRefs.add(appenderRef);
        }
        loggerConfig.appenderRef = appenderRefs.build();

        return loggerConfig;
    }


    public static class LoggerConfig {
        private Level level;
        private ImmutableList<AppenderRef> appenderRef;

        public Level getLevel() {
            return level;
        }

        public ImmutableList<AppenderRef> getAppenderRef() {
            return appenderRef;
        }
    }
}
