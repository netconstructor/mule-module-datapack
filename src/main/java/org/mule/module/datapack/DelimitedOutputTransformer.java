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

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.module.datapack.columns.Column;
import org.mule.module.datapack.i18n.DataPackMessages;
import org.mule.transformer.AbstractMessageTransformer;

import java.util.List;

public class DelimitedOutputTransformer extends AbstractMessageTransformer
{
    private List<Column> columns;
    private Delimiter lineDelimiter = Delimiter.LF;
    private Delimiter delimiter = Delimiter.TAB;
    private Boolean trimToLength = false;
    private Boolean addSpace = false;

    @Override
    public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException
    {
        if (columns == null || columns.size() == 0)
        {
            throw new TransformerException(DataPackMessages.noColumnsDefinedMessage());
        }

        StringBuilder output = new StringBuilder();

        for (int i = 0; i < columns.size(); i++)
        {
            Column column = columns.get(i);
            String value = column.evaluateColumn(message,  muleContext);
            
            String encloseChar = column.getEncloseChar();
            boolean bEnclose = false;
            // Open quote to enclose string
            if (encloseChar != null) {
            	output.append(encloseChar);
            	bEnclose = true;
            }

            if (column.getLength() != null && trimToLength)
            {
                int length = Integer.parseInt(column.getLength());
                int vlen = value.length();
                if (vlen < length)
                	length = vlen;
                output.append(value.substring(0, length));
            }
            else
            {
                output.append(value);
            }
            
            // check to see if a space is needed
            if (addSpace && value.length() == 0)
            {
            	output.append(' ');
            }

            // Closing Quote to enclose string
            if (bEnclose) {
            	output.append(encloseChar);
            }

            
            // column marked as a linebreak
            if (column.getLineBreak() != null && Boolean.parseBoolean(column.getLineBreak()))
            {
            	output.append(getLineDelimiterChar());
            }
            // Only put the delimiter on everything except for the last column or column marked as line break
            else if (i < columns.size() - 1)
            {
                output.append(getDelimiterChar());
            }
        }

        output.append(getLineDelimiterChar());

        return output.toString();
    }


    public List<Column> getColumns()
    {
        return columns;
    }

    public void setColumns(List<Column> columns)
    {
        this.columns = columns;
    }

    public Delimiter getLineDelimiter()
    {
        return lineDelimiter;
    }

    public char getLineDelimiterChar()
    {
        return Delimiter.getChar(lineDelimiter);
    }

    public void setLineDelimiter(Delimiter lineDelimiter)
    {
        this.lineDelimiter = lineDelimiter;
    }

    public Delimiter getDelimiter()
    {
        return delimiter;
    }

    public char getDelimiterChar()
    {
        return Delimiter.getChar(delimiter);
    }

    public void setDelimiter(Delimiter delimiter)
    {
        this.delimiter = delimiter;
    }

    public Boolean getTrimToLength()
    {
        return trimToLength;
    }

    public void setTrimToLength(Boolean trimToLength)
    {
        this.trimToLength = trimToLength;
    }
    
    public Boolean getAddSpace()
    {
    	return addSpace;
    }
    
    public void setAddSpace(Boolean addSpace)
    {
    	this.addSpace = addSpace;
    }
}
