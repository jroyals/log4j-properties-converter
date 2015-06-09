package com.fragstealers.log4j.func;

import com.fragstealers.log4j.xml.binding.Appender;
import com.fragstealers.log4j.xml.binding.Logger;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedMap;

import java.util.Map;

import static com.fragstealers.log4j.func.LoggerConfigFunction.loggerConfigFunction;

/**
 * Created by jason on 4/06/2015.
 */
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
