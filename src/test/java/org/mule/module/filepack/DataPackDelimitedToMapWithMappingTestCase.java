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

public class DataPackDelimitedToMapWithMappingTestCase extends FunctionalTestCase
{
    @Override
    protected String getConfigResources()
    {
        return "datapack-delimited-to-map-with-mappings.xml";
    }

    public void testDelimitedToMapWithMappingLogic() throws Exception
    {
        String data = loadResourceAsString("test-data/test1.csv");
        MuleClient client = new MuleClient(muleContext);
        MuleMessage request = client.send("vm://delimitedToMap.in", data, null);

        assertNotNull(request);
        assertNotNull(request.getPayload());
        assertEquals(21, ((List)request.getPayload()).size());

        Map row = (Map)((List)request.getPayload()).get(0);
        assertEquals(2, row.size());

        assertEquals("Jack-Rogers", row.get("Name"));
        assertEquals("001E000000BZaaLIAT", row.get("Id"));

    }

    public void testDelimitedToMapWithMappingDates() throws Exception
    {
        String data = loadResourceAsString("test-data/test2.csv");
        MuleClient client = new MuleClient(muleContext);
        MuleMessage request = client.send("vm://delimitedToMap2.in", data, null);

        assertNotNull(request);
        assertNotNull(request.getPayload());
        assertEquals(21, ((List)request.getPayload()).size());

        Map row = (Map)((List)request.getPayload()).get(0);
        assertEquals(3, row.size());

        assertEquals("08.31.1933", row.get("DoB"));
        assertEquals("Jack-Rogers", row.get("Name"));
        assertEquals("001E000000BZaaLIAT", row.get("Id"));


    }
}
