//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.05.04 at 07:34:33 AM CEST 
//


package eu.europa.esig.dss.jaxb.detailedreport;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import eu.europa.esig.dss.validation.policy.Context;

public class Adapter6
    extends XmlAdapter<String, Context>
{


    public Context unmarshal(String value) {
        return (eu.europa.esig.dss.jaxb.parsers.ContextParser.parse(value));
    }

    public String marshal(Context value) {
        return (eu.europa.esig.dss.jaxb.parsers.ContextParser.print(value));
    }

}
