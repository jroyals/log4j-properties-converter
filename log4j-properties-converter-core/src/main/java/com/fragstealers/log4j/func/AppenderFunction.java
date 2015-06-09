package com.fragstealers.log4j.func;

import com.fragstealers.log4j.xml.binding.Appender;
import com.fragstealers.log4j.xml.binding.Layout;
import com.fragstealers.log4j.xml.binding.ObjectFactory;
import com.fragstealers.log4j.xml.binding.Param;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.filterKeys;

/**
 * Created by jason on 3/06/2015.
 */
public class AppenderFunction implements Function<Map.Entry<String, String>, Appender> {

    private final Map<String, String> log4jProperties;

    private AppenderFunction(Map<String, String> log4jProperties) {
        this.log4jProperties = log4jProperties;
    }

    public static AppenderFunction appenderFunction(Map<String, String> log4jProperties) {
        return new AppenderFunction(log4jProperties);
    }

    @Override
    public Appender apply(final Map.Entry<String, String> appenderEntry) {
        final Appender appender = new ObjectFactory().createAppender();
        appender.setClazz(appenderEntry.getValue().trim());
        appender.setName(getLastToken(appenderEntry.getKey()).trim());
        appender.setLayout(findLayoutFor(appender).orNull());
        appender.getParam().addAll(extractParams(new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                return input.startsWith(appenderEntry.getKey() + ".") &&
                        !input.startsWith(appenderEntry.getKey() + ".layout");
            }
        }));
        return appender;
    }

    private Optional<Layout> findLayoutFor(final Appender appender) {
        final String layoutKey = "log4j.appender." + appender.getName() + ".layout";
        if (!log4jProperties.containsKey(layoutKey)) {
            return Optional.absent();
        }

        final Layout layout = new ObjectFactory().createLayout();
        layout.setClazz(this.log4jProperties.get(layoutKey.trim()));
        layout.getParam().addAll(extractParams(new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                return input.startsWith(layoutKey + ".");
            }
        }));

        return Optional.of(layout);
    }

    private List<Param> extractParams(Predicate<String> predicate) {
        final Map<String, String> params = filterKeys(log4jProperties, predicate);

        return ImmutableList.copyOf(Iterables.transform(params.entrySet(), new Function<Map.Entry<String, String>, Param>() {
            @Override
            public Param apply(Map.Entry<String, String> input) {
                final Param param = new ObjectFactory().createParam();
                param.setName(getLastToken(input.getKey().trim()));
                param.setValue(input.getValue().trim());
                return param;
            }
        }));
    }

    private static String getLastToken(String paramKey) {
        return paramKey.substring(paramKey.lastIndexOf(".") + 1).trim();
    }
}
