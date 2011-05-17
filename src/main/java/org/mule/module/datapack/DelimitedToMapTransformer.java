/*
 * $Id:$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.datapack;

import org.mule.api.transformer.DataType;
import org.mule.api.transformer.Transformer;
import org.mule.api.transformer.TransformerException;
import org.mule.module.datapack.columns.Column;
import org.mule.module.datapack.i18n.DataPackMessages;
import org.mule.transformer.AbstractTransformer;
import org.mule.transformer.types.DataTypeFactory;
import org.mule.util.StringUtils;

import java.util.HashMap;
import java.util.List;

public class DelimitedToMapTransformer extends AbstractTransformer
{
    private List<Column> columns;
    private String delimiter = ",";

    @Override
    protected Object doTransform(Object src, String enc) throws TransformerException
    {
        if (columns == null || columns.size() == 0)
        {
            throw new TransformerException(DataPackMessages.NoColumnsDefinedMessage());
        }

        // Convert the type to a string if possible.
        String value = null;

        if (src instanceof String)
        {
            value = (String) src;
        }
        else
        {
            Transformer transformer = muleContext.getRegistry().lookupTransformer(DataTypeFactory.create(src.getClass()),
                    DataType.STRING_DATA_TYPE);

            if (transformer != null)
            {
                value = (String) transformer.transform(src);
            }
            else
            {
                throw new TransformerException(DataPackMessages.NotAbleToConvertPayloadToString());
            }
        }

        String[] tokens = value.split(delimiter);

        if (columns.size() != tokens.length)
        {
            throw new TransformerException(DataPackMessages.ColumnSizeMisMatch());
        }

        HashMap<String, String> map = new HashMap<String, String>();


        for (int i = 0; i < tokens.length; i++)
        {
            if (StringUtils.isEmpty(columns.get(i).getColumnName()))
            {
                throw new TransformerException(DataPackMessages.NoColumnNameDefined());
            }

            map.put(columns.get(i).getColumnName(), tokens[i].trim());
        }

        return map;
    }

    public List<Column> getColumns()
    {
        return columns;
    }

    public void setColumns(List<Column> columns)
    {
        this.columns = columns;
    }

    public String getDelimiter()
    {
        return delimiter;
    }

    public void setDelimiter(String delimiter)
    {
        this.delimiter = delimiter;
    }
}
