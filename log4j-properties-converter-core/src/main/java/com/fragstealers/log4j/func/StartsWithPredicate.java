package com.fragstealers.log4j.func;

import com.google.common.base.Predicate;

/**
 * Created by jason on 4/06/2015.
 */
public class StartsWithPredicate implements Predicate<String> {
    private final String prefix;

    private StartsWithPredicate(String prefix) {
        this.prefix = prefix;
    }

    public static StartsWithPredicate startsWith(String prefix) {
        return new StartsWithPredicate(prefix);
    }

    @Override
    public boolean apply(String input) {
        return input.startsWith(prefix);
    }
}
