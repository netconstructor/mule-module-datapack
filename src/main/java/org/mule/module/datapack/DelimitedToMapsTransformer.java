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
import org.mule.transformer.types.DataTypeFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class DelimitedToMapsTransformer extends DelimitedToMapTransformer
{
    private String lineDelimiter = "\n";
    private int readFromLine = 1;

    @Override
    protected Object doTransform(Object src, String enc) throws TransformerException
    {
        String value;

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
                throw new TransformerException(DataPackMessages.notAbleToConvertPayloadToString());
            }
        }

        List<String> lines = Arrays.asList(value.split(lineDelimiter));

        List<Map<String, String>> maps = new ArrayList<Map<String, String>>();

        int i = 1;
        for (String line : lines)
        {
            if(i >= getReadFromLine())
            {
                maps.add((Map<String, String>)super.doTransform(line, enc));
            }
            i++;
        }

        return maps;
    }

    public String getLineDelimiter()
    {
        return lineDelimiter;
    }

    public void setLineDelimiter(String lineDelimiter)
    {
        this.lineDelimiter = lineDelimiter;
    }

    public int getReadFromLine()
    {
        return readFromLine;
    }

    public void setReadFromLine(int readFromLine)
    {
        this.readFromLine = readFromLine;
    }
}
