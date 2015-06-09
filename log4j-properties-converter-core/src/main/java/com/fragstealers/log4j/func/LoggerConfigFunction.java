package com.fragstealers.log4j.func;

import com.fragstealers.log4j.xml.binding.Appender;
import com.fragstealers.log4j.xml.binding.AppenderRef;
import com.fragstealers.log4j.xml.binding.Level;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * Created by jason on 9/06/2015.
 */
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
