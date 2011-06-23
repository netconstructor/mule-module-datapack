The Mule Datapack's project is a set of tools which help with the processing of tabular type data. This includes the
formatting of output in either delimited text or in a fixed with format. There are also tools which enable the parsing
of delimted files.

First in order to do any processing the mapping of the columns must be defined.  This is done in the configuration of Mule.
The following is a sample of a simple configuration:

    <?xml version="1.0" encoding="UTF-8"?>
    <mule xmlns="http://www.mulesoft.org/schema/mule/core"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xmlns:datapack="http://www.mulesoft.org/schema/mule/datapack"
          xmlns:vm="http://www.mulesoft.org/schema/mule/vm"
        xsi:schemaLocation="
            http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/3.1/mule.xsd
            http://www.mulesoft.org/schema/mule/datapack http://www.mulesoft.org/schema/mule/datapack/3.1/mule-datapack.xsd
            http://www.mulesoft.org/schema/mule/vm http://www.mulesoft.org/schema/mule/vm/3.1/mule-vm.xsd
            ">

        <flow name="delimitedToMap">
            <http:inbound-endpoint address="http://localhost:8081/datapack"/>

            <datapack:delimited-to-map-transformer delimiter="\t">
                <datapack:column column-name="value1"/>
                <datapack:column column-name="value2"/>
            </datapack:delimited-to-map-transformer>
        </flow>
    </mule>

