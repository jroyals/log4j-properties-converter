package com.fragstealers.log4j.jaxb.builder

import com.fragstealers.log4j.xml.binding.Param

class BuilderUtils {

    static void addParam(def objectToAdd, String fullName, String value) {
        String name = fullName.tokenize(".").last()
        objectToAdd.getParam().add(new Param(name: name, value: value))
    }

    static boolean isValidLoggingLevel(String level) {
        try {
            Levels.valueOf(Levels.class, level?.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
