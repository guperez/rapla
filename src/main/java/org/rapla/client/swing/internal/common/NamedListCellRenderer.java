/*--------------------------------------------------------------------------*
 | Copyright (C) 2014 Christopher Kohlhaas                                  |
 |                                                                          |
 | This program is free software; you can redistribute it and/or modify     |
 | it under the terms of the GNU General Public License as published by the |
 | Free Software Foundation. A copy of the license has been included with   |
 | these distribution in the COPYING file, if not go to www.fsf.org         |
 |                                                                          |
 | As a special exception, you are granted the permissions to link this     |
 | program with every library, which license fulfills the Open Source       |
 | Definition as published by the Open Source Initiative (OSI).             |
 *--------------------------------------------------------------------------*/
package org.rapla.client.swing.internal.common;

import org.rapla.entities.Named;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import java.awt.Component;
import java.text.MessageFormat;
import java.util.Locale;

public class NamedListCellRenderer extends DefaultListCellRenderer {
    private static final long serialVersionUID = 1L;
    
    Locale locale;
    MessageFormat format = null;

    public NamedListCellRenderer(Locale locale) {
        this.locale = locale;
    }

    public NamedListCellRenderer(Locale locale,String formatString) {
        this(locale);
        this.format = new MessageFormat(formatString);
    }


    public Component getListCellRendererComponent(JList list,
                                                  Object value,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus) {
        Object newValue;
        if (value instanceof Named)
        {
            newValue = ((Named) value).getName(locale);
        }
        else
        {
            newValue = value;
        }
        if (format != null)
            newValue = format.format(new Object[] {newValue});
        return super.getListCellRendererComponent(list,newValue,index,isSelected,cellHasFocus);
    }
}