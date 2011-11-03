/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.datapack.mapper;

import org.mule.DefaultMuleMessage;
import org.mule.api.MuleContext;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.module.datapack.columns.Column;

import java.util.Map;

/**
 * Defines a single mapping rule when mapping between two Maps.  typically a Map represents a row of data.
 * A mapping allows columns to be renamed, direct mapping and using expressions to combine column values or perfromaing other mapping logic
 */
public class Mapping
{
    public static final String MAP_COLUMN_PATTERN = "#[map-payload:%s]";

    protected String sourceColumn;
    protected Column destinationColumn = new Column();

    public Mapping()
    {
    }

    public Mapping(String sourceColumn, Column destinationColumn)
    {
        this.sourceColumn = sourceColumn;
        this.destinationColumn = destinationColumn;
    }

    public void evaluate(Map<String, Object> src, Map<String, Object> dest, MuleContext muleContext) throws TransformerException
    {
        MuleMessage message = new DefaultMuleMessage(src, muleContext);
        if(sourceColumn!=null) destinationColumn.setValue(String.format(MAP_COLUMN_PATTERN, sourceColumn));
        String value = destinationColumn.evaluateColumn(message, muleContext);
        dest.put(destinationColumn.getColumnName(), value);
    }

    public String getSourceColumn()
    {
        return sourceColumn;
    }

    public void setSourceColumn(String sourceColumn)
    {
        this.sourceColumn = sourceColumn;
    }

    public String getDestinationColumn()
    {
        return destinationColumn.getColumnName();
    }

    public void setDestinationColumn(String destinationColumn)
    {
        this.destinationColumn.setColumnName(destinationColumn);
    }

    public String getValue()
    {
        return destinationColumn.getValue();
    }

    public void setValue(String value)
    {
        this.destinationColumn.setValue(value);
    }

    public String getDefaultValue()
    {
        return destinationColumn.getDefaultValue();
    }

    public void setDefaultValue(String defaultValue)
    {
        this.destinationColumn.setDefaultValue(defaultValue);
    }
}
