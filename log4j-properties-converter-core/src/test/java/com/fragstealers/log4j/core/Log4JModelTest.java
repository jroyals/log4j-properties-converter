package com.fragstealers.log4j.core;

import com.fragstealers.log4j.xml.binding.Appender;
import com.fragstealers.log4j.xml.binding.Layout;
import com.fragstealers.log4j.xml.binding.Logger;
import com.fragstealers.log4j.xml.binding.Param;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.List;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

/**
 * Created by jason on 3/06/2015.
 */
public class Log4JModelTest {

    @Test
    public void shouldExtractSingleSimpleAppender() {
        Properties props = new Properties();
        props.setProperty("log4j.appender.foo", "com.fragstealers.Test");

        Log4JModel model = new Log4JModel(props);

        List<Appender> appenders = model.getAppenders();

        assertThat(appenders.size(), is(1));
        Appender appender = appenders.get(0);

        assertThat(appender.getName(), is("foo"));
        assertThat(appender.getClazz(), is("com.fragstealers.Test"));
    }

    @Test
    public void shouldExtractSingleAppenderWithParams() {
        Properties props = new Properties();
        props.setProperty("log4j.appender.foo", "com.fragstealers.Test");
        props.setProperty("log4j.appender.foo.1", "x");
        props.setProperty("log4j.appender.foo.2", "y");
        props.setProperty("log4j.appender.foo.3", "z.a");

        Log4JModel model = new Log4JModel(props);

        List<Appender> appenders = model.getAppenders();

        assertThat(appenders.size(), is(1));
        Appender appender = appenders.get(0);

        assertThat(appender.getName(), is("foo"));
        assertThat(appender.getClazz(), is("com.fragstealers.Test"));
        final List<Param> param = appender.getParam();
        assertThat(param.size(), is(3));
        assertThat(param.get(0).getName(), is("1"));
        assertThat(param.get(0).getValue(), is("x"));
        assertThat(param.get(1).getName(), is("2"));
        assertThat(param.get(1).getValue(), is("y"));
        assertThat(param.get(2).getName(), is("3"));
        assertThat(param.get(2).getValue(), is("z.a"));
    }

    @Test
    public void shouldExtractSingleAppenderWithSimpleLayout() {
        Properties props = new Properties();
        props.setProperty("log4j.appender.foo", "com.fragstealers.Test");
        props.setProperty("log4j.appender.foo.layout", "com.fragstealers.Layout");

        Log4JModel model = new Log4JModel(props);

        List<Appender> appenders = model.getAppenders();

        assertThat(appenders.size(), is(1));
        Appender appender = appenders.get(0);

        assertThat(appender.getName(), is("foo"));
        assertThat(appender.getClazz(), is("com.fragstealers.Test"));
        final List<Param> param = appender.getParam();
        assertThat(param.size(), is(0));

        Layout layout = appender.getLayout();
        assertThat(layout.getClazz(), is("com.fragstealers.Layout"));
    }

    @Test
    public void shouldExtractSingleAppenderWithComplexLayout() {
        Properties props = new Properties();
        props.setProperty("log4j.appender.foo", "com.fragstealers.Test");
        props.setProperty("log4j.appender.foo.layout", "com.fragstealers.Layout");
        props.setProperty("log4j.appender.foo.layout.1", "x");
        props.setProperty("log4j.appender.foo.layout.2", "y");

        Log4JModel model = new Log4JModel(props);

        List<Appender> appenders = model.getAppenders();

        assertThat(appenders.size(), is(1));
        Appender appender = appenders.get(0);

        assertThat(appender.getName(), is("foo"));
        assertThat(appender.getClazz(), is("com.fragstealers.Test"));
        assertThat(appender.getParam(), Matchers.empty());

        Layout layout = appender.getLayout();
        assertThat(layout.getClazz(), is("com.fragstealers.Layout"));
        List<Param> layoutParam = layout.getParam();
        assertThat(layoutParam, hasSize(2));
        assertThat(layoutParam.get(0).getName(), is("1"));
        assertThat(layoutParam.get(0).getValue(), is("x"));
        assertThat(layoutParam.get(1).getName(), is("2"));
        assertThat(layoutParam.get(1).getValue(), is("y"));
    }

    @Test
    public void shouldExtractBasicLogger() {
        Properties props = new Properties();
        props.setProperty("log4j.logger.foo.bar.baz", "debug");

        Log4JModel model = new Log4JModel(props);

        List<Logger> loggers = model.getLoggers();

        assertThat(loggers.size(), is(1));
        Logger logger = loggers.get(0);

        assertThat(logger.getName(), is("foo.bar.baz"));
        assertThat(logger.getLevel().getValue(), is("DEBUG"));
    }

    @Test
    public void shouldExtractBasicLoggerWithAppenderRefs() {
        Properties props = new Properties();
        props.setProperty("log4j.logger.foo.bar.baz", "debug , ap1 , ap2");
        props.setProperty("log4j.appender.ap1", "foo");
        props.setProperty("log4j.appender.ap2", "bar");

        Log4JModel model = new Log4JModel(props);

        List<Logger> loggers = model.getLoggers();

        assertThat(loggers.size(), is(1));
        Logger logger = loggers.get(0);

        assertThat(logger.getName(), is("foo.bar.baz"));
        assertThat(logger.getLevel().getValue(), is("DEBUG"));
        assertThat(logger.getAppenderRef(), hasSize(2));
        assertThat(((Appender) logger.getAppenderRef().get(0).getRef()).getName(), is("ap1"));
        assertThat(((Appender) logger.getAppenderRef().get(1).getRef()).getName(), is("ap2"));
    }

    @Test
    public void shouldExtractBasicLoggerWithAdditivity() {
        Properties props = new Properties();
        props.setProperty("log4j.logger.foo.bar.baz", "debug");
        props.setProperty("log4j.additivity.foo.bar.baz", "true");

        Log4JModel model = new Log4JModel(props);

        List<Logger> loggers = model.getLoggers();

        assertThat(loggers.size(), is(1));
        Logger logger = loggers.get(0);

        assertThat(logger.getName(), is("foo.bar.baz"));
        assertThat(logger.getLevel().getValue(), is("DEBUG"));
        assertThat(logger.getAdditivity(), is("true"));
    }

    @Test
    public void shouldDealWithLoggersNamedAsCategories() {
        Properties props = new Properties();
        props.setProperty("log4j.category.foo.bar.baz", "debug");

        Log4JModel model = new Log4JModel(props);

        List<Logger> loggers = model.getLoggers();

        assertThat(loggers.size(), is(1));
        Logger logger = loggers.get(0);

        assertThat(logger.getName(), is("foo.bar.baz"));
        assertThat(logger.getLevel().getValue(), is("DEBUG"));
    }
}
