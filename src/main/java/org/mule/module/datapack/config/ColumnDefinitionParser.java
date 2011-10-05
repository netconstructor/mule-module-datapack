/*
 * $Id:$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.datapack.config;

import org.mule.config.spring.parsers.generic.ChildDefinitionParser;

public class ColumnDefinitionParser extends ChildDefinitionParser
{
    public ColumnDefinitionParser(String setterMethod, Class<?> clazz)
    {
        super(setterMethod, clazz);
        this.addAlias("column-name", "columnName");
        this.addAlias("default-value", "defaultValue");
        this.addAlias("pad-char", "padChar");
        this.addAlias("line-break", "lineBreak");
        this.addAlias("enclose-char", "encloseChar");
    }
}
