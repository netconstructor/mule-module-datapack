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
    public static final String CURRENT_VALUE_PATTERN = "#[map-payload:%s]";
    private String columnName = null;
    private String mappedColumnName = null;
    private String length;
    private String value;
    private String columnIndex;
    private String defaultValue;
    private String padChar = null;
    private String lineBreak;
    private String encloseChar;
    private boolean skipColumn = false;

    protected final TemplateParser.PatternInfo patternInfo = TemplateParser.createMuleStyleParser().getStyle();



    public String evaluateColumn(MuleMessage message, MuleContext muleContext) throws TransformerException
    {
        //If no value is set, default to the original column value
        if(value==null) value = String.format(CURRENT_VALUE_PATTERN, columnName);

        String result = evaluate(value, message, muleContext);

        if (StringUtils.isEmpty(result) && !StringUtils.isEmpty(defaultValue))
        {
            result = evaluate(defaultValue, message, muleContext);
        }

        return result;
    }

    protected String evaluate(String expression, MuleMessage message, MuleContext muleContext) throws TransformerException
    {
        ExpressionManager expressionManager = muleContext.getExpressionManager();

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
    
    public String getLineBreak()
    {
        return lineBreak;
    }

    public void setLineBreak(String lineBreak)
    {
        this.lineBreak = lineBreak;
    }
    
    public String getEncloseChar()
    {
        return encloseChar;
    }

    public void setEncloseChar(String encloseChar)
    {
        this.encloseChar = encloseChar;
    }

    public String getMappedColumnName()
    {
        return (mappedColumnName ==null ? getColumnName() : mappedColumnName);
    }

    public void setMappedColumnName(String mappedColumnName)
    {
        this.mappedColumnName = mappedColumnName;
    }

    public boolean isSkipColumn()
    {
        return skipColumn;
    }

    public void setSkipColumn(boolean skipColumn)
    {
        this.skipColumn = skipColumn;
    }
}
