package com.fragstealers.log4j.jaxb.builder

import generated.Param

////////////////////////////////////////////////////////////////////////////////
//
// Copyright (c) 2010, Suncorp Metway Limited. All rights reserved.
//
// This is unpublished proprietary source code of Suncorp Metway Limited.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
////////////////////////////////////////////////////////////////////////////////
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
