/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.filepack;

import org.mule.api.transformer.Transformer;
import org.mule.module.datapack.MappingTransformer;
import org.mule.module.datapack.columns.Column;
import org.mule.module.datapack.mapper.Mapping;
import org.mule.transformer.AbstractTransformerTestCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tests the Mapping transformer will process valid and invalid payload consistently
 */
public class MapperTransformerTestCase extends AbstractTransformerTestCase
{
    @Override
    public Transformer getTransformer() throws Exception
    {
        MappingTransformer t = createObject(MappingTransformer.class);
        Column c1 = new Column();
        c1.setColumnName("oof");
        c1.setValue("#[groovy:payload.foo.toUpperCase()]");
        t.getMappings().add(new Mapping(null, c1));

        Column c2 = new Column();
        c2.setColumnName("ood");
        c2.setValue("#[groovy:payload.doo.toUpperCase()]");
        t.getMappings().add(new Mapping(null, c2));

        return t;
    }

    @Override
    public Transformer getRoundTripTransformer() throws Exception
    {
        return null;
    }

    @Override
    public Object getTestData()
    {
        Map row1 = new HashMap();
        row1.put("foo", "bar");
        row1.put("doo", "dar");
        List data = new ArrayList();
        data.add(row1);
        return data;
    }

    @Override
    public Object getResultData()
    {
        Map row1 = new HashMap();
        row1.put("oof", "BAR");
        row1.put("ood", "DAR");
        List data = new ArrayList();
        data.add(row1);
        return data;
    }
}
