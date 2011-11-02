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

import java.util.List;
import java.util.Map;

public class DataPackDelimitedToMapWithSimpleMappingTestCase extends FunctionalTestCase
{
    @Override
    protected String getConfigResources()
    {
        return "datapack-delimited-to-map-with-simple-mappings.xml";
    }

    public void testDelimitedToMapWithFieldMapping() throws Exception
    {
        String data = loadResourceAsString("test-data/test1.csv");
        MuleClient client = new MuleClient(muleContext);
        MuleMessage request = client.send("vm://delimitedToMap.in", data, null);

        assertNotNull(request);
        assertNotNull(request.getPayload());
        assertEquals(((List)request.getPayload()).size(), 21);

        Map row = (Map)((List)request.getPayload()).get(0);
        assertEquals(3, row.size());

        assertEquals("Jack", row.get("FirstName"));
        assertEquals("Rogers", row.get("LastName"));
        assertEquals("001E000000BZaaLIAT", row.get("Id"));

    }
}
