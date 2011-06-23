/*
 * $Id:$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.datapack.columns;

import org.mule.api.MuleContext;
import org.mule.api.MuleMessage;
import org.mule.api.expression.ExpressionManager;
import org.mule.api.transformer.TransformerException;
import org.mule.module.datapack.i18n.DataPackMessages;
import org.mule.util.TemplateParser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeColumn extends Column
{
    public String dateFormatIn;
    public String dateFormatOut;

    @Override
    public String evaluateColumn(MuleMessage message, MuleContext muleContext, ExpressionManager expressionManager, TemplateParser.PatternInfo patternInfo) throws TransformerException
    {
        String value = super.evaluateColumn(message, muleContext, expressionManager, patternInfo);

        DateFormat dfIn = new SimpleDateFormat(dateFormatIn);
        Date dateIn = null;

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

            return dfOut.format(dateIn);
        }
        else
        {
            return null;
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
