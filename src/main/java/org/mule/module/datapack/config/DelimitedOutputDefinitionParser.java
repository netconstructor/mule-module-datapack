/*
 * $Id:$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.datapack.config;

import org.mule.config.spring.parsers.specific.MessageProcessorDefinitionParser;
import org.mule.module.datapack.DelimitedOutputTransformer;

public class DelimitedOutputDefinitionParser extends MessageProcessorDefinitionParser
{
    public DelimitedOutputDefinitionParser()
    {
        super(DelimitedOutputTransformer.class);
        addAlias("newline-char", "newlineChar");
        addAlias("delimiter-char", "delimiterChar");
        addAlias("trim-to-length", "trimToLength");
        addAlias("add-space", "addSpace");
    }
}
