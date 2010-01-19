package com.fragstealers.log4j.ui

import groovy.swing.SwingBuilder
import java.awt.BorderLayout as BL

////////////////////////////////////////////////////////////////////////////////
//
// Copyright (c) 2010, Suncorp Metway Limited. All rights reserved.
//
// This is unpublished proprietary source code of Suncorp Metway Limited.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
////////////////////////////////////////////////////////////////////////////////
class UI {
    def showUi() {
        def swing = new SwingBuilder()
        def count = 0
        def textlabel
        def frame = swing.frame(title:'Frame', size:[300,300]) {
          borderLayout()
          textlabel = label(text:"Click the button!", constraints: BL.NORTH)
          button(text:'Click Me',
                 actionPerformed: {count++; textlabel.text = "Clicked ${count} time(s)."; println "clicked"},
                 constraints:BL.SOUTH)
        }
        frame.show()
        
    }
}
