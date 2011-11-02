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
import org.mule.api.transformer.DataType;
import org.mule.api.transformer.Transformer;
import org.mule.api.transformer.TransformerException;
import org.mule.module.datapack.i18n.DataPackMessages;
import org.mule.transformer.types.DataTypeFactory;

public class TransformColumn extends Column
{
    private String transformerName;

    @Override
    public String evaluateColumn(MuleMessage message, MuleContext muleContext) throws TransformerException
    {
        Transformer transformer = muleContext.getRegistry().lookupTransformer(transformerName);

        if (transformer == null)
        {
            throw new TransformerException(DataPackMessages.noTransformerFound(transformerName));
        }

        String value = super.evaluateColumn(message, muleContext);

        // Don't do the transformation if the original value was null since we will be doing the transformation
        // on the default value.
        if (getValue() != null)
        {
            Object result = transformer.transform(value);

            // If the output isn't a string then try to transform it to a string for the output.
            if (result instanceof String)
            {
                value = (String) result;
            }
            else
            {
                Transformer stringTransformer = muleContext.getRegistry().lookupTransformer(DataTypeFactory.create(result.getClass()),
                        DataType.STRING_DATA_TYPE);

                if (stringTransformer != null)
                {
                    value = (String) stringTransformer.transform(result);
                }
                else
                {
                    throw new TransformerException(DataPackMessages.notAbleToConvertToString(getColumnName()));
                }
            }
        }

        return value;
    }

    public String getTransformerName()
    {
        return transformerName;
    }

    public void setTransformerName(String transformerName)
    {
        this.transformerName = transformerName;
    }
}
