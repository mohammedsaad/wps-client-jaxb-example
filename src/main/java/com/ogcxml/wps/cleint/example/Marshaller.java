package com.ogcxml.wps.cleint.example;

import net.opengis.gml.v_3_2_1.FeatureCollectionType;
import net.opengis.sampling.v_2_0.SFSamplingFeatureType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.io.StringWriter;

public class Marshaller {
    private static JAXBContext context;
    public String marshal(JAXBElement <FeatureCollectionType> xml) throws JAXBException {
        context = JAXBContext.newInstance(FeatureCollectionType.class.getPackage()
                .getName()
                + ":"
                + FeatureCollectionType.class.getPackage().getName() + ":"
                + SFSamplingFeatureType.class.getPackage().getName());

        // Marshall.
        javax.xml.bind.Marshaller marshaller = context.createMarshaller();
        //marshaller.marshal(createFeatureCollection(), System.out);
        java.io.StringWriter sw = new StringWriter();
        // deletes the
        marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
        marshaller.marshal(xml, sw);
        return sw.toString();
    }
}
