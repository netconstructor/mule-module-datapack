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
import org.mule.module.datapack.i18n.DataPackMessages;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * A DateMapping is a mapping rule specialied for working with transforming date formats
 */
public class DateMapping extends Mapping
{
    public static final String CURRENT_VALUE_PATTERN = "#[map-payload:%s]";

    private String dateFormatIn;
    private String dateFormatOut;

    public DateMapping()
    {
    }

    public DateMapping(String sourceColumn, Column destinationColumn, String dateFormatIn, String dateFormatOut)
    {
        super(sourceColumn, destinationColumn);
        this.dateFormatIn = dateFormatIn;
        this.dateFormatOut = dateFormatOut;
    }


    @Override
    public void evaluate(Map<String, Object> src, Map<String, Object> dest, MuleContext muleContext) throws TransformerException
    {
        MuleMessage message = new DefaultMuleMessage(src, muleContext);

        if (destinationColumn.getValue() == null)
        {
            destinationColumn.setValue(String.format(CURRENT_VALUE_PATTERN, sourceColumn));
        }

        String value = destinationColumn.evaluateColumn(message, muleContext);
        Date dateIn = null;

        if (value != null && !value.equalsIgnoreCase("null") && value.length() > 0)
        {
            DateFormat dfIn = new SimpleDateFormat(dateFormatIn);

            try
            {
                dateIn = dfIn.parse(value);
            }
            catch (ParseException e)
            {
                DataPackMessages.dateInboundParseError(value, dateFormatIn);
            }

            if (dateIn != null)
            {
                DateFormat dfOut = new SimpleDateFormat(dateFormatOut);
                dest.put(destinationColumn.getColumnName(), dfOut.format(dateIn));
            }
        }
        else
        {
            dest.put(destinationColumn.getColumnName(), null);
        }
    }

    public String getDateFormatIn()
    {
        return dateFormatIn;
    }

    public void setDateFormatIn(String dateFormatIn)
    {
        this.dateFormatIn = dateFormatIn;
    }

    public String getDateFormatOut()
    {
        return dateFormatOut;
    }

    public void setDateFormatOut(String dateFormatOut)
    {
        this.dateFormatOut = dateFormatOut;
    }
}
