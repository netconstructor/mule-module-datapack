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
import org.mule.api.transformer.DataType;
import org.mule.api.transformer.Transformer;
import org.mule.api.transformer.TransformerException;
import org.mule.module.datapack.i18n.DataPackMessages;
import org.mule.transformer.types.DataTypeFactory;
import org.mule.util.StringUtils;
import org.mule.util.TemplateParser;

public class Column
{
    public String columnName = null;
    public String length;
    public String value;
    public String columnIndex;
    public String defaultValue;
    public String padChar = null;


    public String evaluateColumn(MuleMessage message, MuleContext muleContext, ExpressionManager expressionManager,
                                 TemplateParser.PatternInfo patternInfo) throws TransformerException
    {
        String result = evaluate(value, message, muleContext, expressionManager, patternInfo);

        if (StringUtils.isEmpty(result) && !StringUtils.isEmpty(defaultValue))
        {
            result = evaluate(defaultValue, message, muleContext, expressionManager, patternInfo);
        }

        return result;
    }

    protected String evaluate(String expression, MuleMessage message, MuleContext muleContext, ExpressionManager expressionManager,
                                 TemplateParser.PatternInfo patternInfo) throws TransformerException
    {
        Object evaluatedValue;

        // If string contains is a single expression then evaluate otherwise
        // parse. We can't use parse() always because that will convert
        // everything to a string
        if (expression.startsWith(patternInfo.getPrefix())
                && expression.endsWith(patternInfo.getSuffix()))
        {
            evaluatedValue = expressionManager.evaluate(expression, message);
        }
        else
        {
            evaluatedValue = expressionManager.parse(expression, message);
        }

        String result;

        // Get the value into a string since that is the final output.
        if (evaluatedValue == null)
        {
            result = "";
        }
        else if (evaluatedValue instanceof String)
        {
            result = (String) evaluatedValue;
        }
        else
        {
            Transformer transformer = muleContext.getRegistry().lookupTransformer(DataTypeFactory.create(evaluatedValue.getClass()),
                    DataType.STRING_DATA_TYPE);

            if (transformer != null)
            {
                result = (String) transformer.transform(evaluatedValue);
            }
            else
            {
                throw new TransformerException(DataPackMessages.notAbleToConvertToString(columnName));
            }
        }

        return result.trim();
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public String getLength()
    {
        return length;
    }

    public void setLength(String length)
    {
        this.length = length;
    }

    public String getColumnName()
    {
        return columnName;
    }

    public void setColumnName(String columnName)
    {
        this.columnName = columnName;
    }

    public String getColumnIndex()
    {
        return columnIndex;
    }

    public void setColumnIndex(String columnIndex)
    {
        this.columnIndex = columnIndex;
    }

    public String getDefaultValue()
    {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }

    public String getPadChar()
    {
        return padChar;
    }

    public void setPadChar(String padChar)
    {
        this.padChar = padChar;
    }
}
