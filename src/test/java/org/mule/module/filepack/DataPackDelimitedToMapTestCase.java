/*
 * $Id:$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.filepack;

import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import org.mule.tck.FunctionalTestCase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataPackDelimitedToMapTestCase extends FunctionalTestCase
{
    @Override
    protected String getConfigResources()
    {
        return "datapack-delimited-to-map-test-config.xml";
    }

    public void testDelimitedToMap() throws Exception
    {
        MuleClient client = new MuleClient(muleContext);
        MuleMessage request = client.send("vm://delimitedToMap.in", "foo\tbar\rdata\tdata", null);

        assertNotNull(request);
        assertNotNull(request.getPayload());
        assertEquals(1, ((List) request.getPayload()).size());
        Map row = (HashMap)((List) request.getPayload()).get(0);
        assertEquals(2, row.keySet().size());
    }
}
