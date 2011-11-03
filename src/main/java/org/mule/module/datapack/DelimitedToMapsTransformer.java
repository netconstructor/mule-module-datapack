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
import org.mule.module.datapack.i18n.DataPackMessages;
import org.mule.transformer.AbstractTransformer;
import org.mule.transformer.types.DataTypeFactory;

import com.csv.CsvReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DelimitedToMapsTransformer extends AbstractTransformer
{
    private Delimiter lineDelimiter;
    private Delimiter delimiter = Delimiter.COMMA;
    private boolean skipEmptyRows = true;
    private boolean trimWhitespace = true;


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

        CsvReader reader = new CsvReader(new ByteArrayInputStream(value.getBytes()), Delimiter.getChar(getDelimiter()), Charset.forName(enc));

        List<Map<String, String>> maps = new ArrayList<Map<String, String>>();

        try
        {
            if(lineDelimiter!=null) reader.setRecordDelimiter(Delimiter.getChar(lineDelimiter));

            reader.setSkipEmptyRecords(isSkipEmptyRows());
            reader.setTrimWhitespace(isTrimWhitespace());
            reader.readHeaders();

            String[] headers = reader.getHeaders();
            if(headers==null)
            {
                throw new TransformerException(DataPackMessages.noCSVHeaders());
            }
            
            while(reader.readRecord())
            {
                Map<String, String> row = new HashMap<String, String>();
                String[] values = reader.getValues();

                for (int j = 0; j < headers.length; j++)
                {
                     row.put(headers[j], values[j]);
                }
                maps.add(row);
            }
        }
        catch (IOException e)
        {
            throw new TransformerException(this, e);
        }
        finally
        {
            reader.close();
        }

        return maps;
    }

    public Delimiter getLineDelimiter()
    {
        return lineDelimiter;
    }

    public void setLineDelimiter(Delimiter lineDelimiter)
    {
        this.lineDelimiter = lineDelimiter;
    }

    public boolean isSkipEmptyRows()
    {
        return skipEmptyRows;
    }

    public void setSkipEmptyRows(boolean skipEmptyRows)
    {
        this.skipEmptyRows = skipEmptyRows;
    }

    public boolean isTrimWhitespace()
    {
        return trimWhitespace;
    }

    public void setTrimWhitespace(boolean trimWhitespace)
    {
        this.trimWhitespace = trimWhitespace;
    }

    public Delimiter getDelimiter()
    {
        return delimiter;
    }

    public void setDelimiter(Delimiter delimiter)
    {
        this.delimiter = delimiter;
    }
}
