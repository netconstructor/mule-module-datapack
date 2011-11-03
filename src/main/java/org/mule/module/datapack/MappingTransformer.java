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
import org.mule.api.transformer.TransformerException;
import org.mule.module.datapack.mapper.Mapping;
import org.mule.transformer.AbstractTransformer;
import org.mule.transformer.types.CollectionDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A transformer for converting data and structure held in a list of Maps.
 *
 * Many projects that work with tabular data will use a {@link List} of {@link java.util.Map} objects
 * where each map is a row. In Mule the JDBC, Salesforce, Datapack and HL7 connectors all use this
 * format.  This transformer allows simple mapping rules to be created to alter these list of maps.
 *
 * The transformer will still return a list of maps but the number of fields, field names and vlaue can
 * be changed.
 *
 * For example, for simple one-to-one field mapping it can be used as:
 *
 *
 * <code>
 *     <datapack:mapping-transformer >
            <datapack:mapping destinationColumn="Id" sourceColumn="AccountId"/>
            <datapack:mapping destinationColumn="LastName" sourceColumn="LastName"/>
            <datapack:mapping destinationColumn="FirstName" sourceColumn="FirstName"/>
        </datapack:mapping-transformer>
 * </code>
 *
 * or to combine fields an expression can be used:
 *
 * <code>
 *     <datapack:mapping-transformer >
            <datapack:mapping destinationColumn="Id" sourceColumn="AccountId"/>
            <datapack:mapping destinationColumn="Name" value="#[groovy:payload.FirstName + '-' + payload.LastName]"/>
        </datapack:mapping-transformer>
 * </code>
 *
 * If sourceColumn is defined it will override anything set in the 'value' attribute. You can also set a
 * static default value for a field.
 *
 * Note when defining mapping rules it is don in the context of  what you want the destination data to look like.
 * As such, you need to specify a rule for every field you want in the resulting data set.  each mapping rule will
 * be applied to very row (map) in the data set.
 *
 * @since 3.2
 */
public class MappingTransformer extends AbstractTransformer
{
    private List<Mapping> mappings = new ArrayList<Mapping>();

    public MappingTransformer()
    {
        DataType dt = new CollectionDataType(List.class, Map.class);
        registerSourceType(dt);
        setReturnDataType(dt);
    }

    @Override
    protected Object doTransform(Object src, String enc) throws TransformerException
    {
        List<Map<String, Object>> data = (List<Map<String, Object>>)src;
        List<Map<String, Object>> mappedData = new ArrayList<Map<String, Object>>(data.size());

        for (Map<String, Object> row : data)
        {
            Map<String, Object> mappedRow = new HashMap<String, Object>(row.size());
            for (Mapping mapping : mappings)
            {
                mapping.evaluate(row, mappedRow, muleContext);
            }
            mappedData.add(mappedRow);
        }
        return mappedData;
    }

    public List<Mapping> getMappings()
    {
        return mappings;
    }

    public void setMappings(List<Mapping> mappings)
    {
        this.mappings = mappings;
    }
}