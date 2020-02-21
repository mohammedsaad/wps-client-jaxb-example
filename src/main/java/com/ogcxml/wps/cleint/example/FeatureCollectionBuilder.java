package com.ogcxml.wps.cleint.example;

import net.opengis.gml.v_3_2_1.*;
import net.opengis.sampling.v_2_0.SFSamplingFeatureType;

import javax.xml.bind.JAXBElement;

public class FeatureCollectionBuilder {

    public FeatureCollectionBuilder() {
    }

    /**
     * this method builds an object of the following schema
     * <gml:Polygon>
     * <gml:exterior>
     * <gml:LinearRing>
     * <gml:posList>1.0 1.0</gml:posList>
     * </gml:LinearRing>
     * </gml:exterior>
     * <gml:interior>
     * <gml:LinearRing>
     * <gml:posList>1.0 1.0</gml:posList>
     * </gml:LinearRing>
     * </gml:interior>
     * </gml:Polygon>
     *
     */
    public JAXBElement <FeatureCollectionType> createFeatureCollection() {
        ObjectFactory gmlFactory = new ObjectFactory();
        //create linear ring with coordinates
        CoordinatesType coordinatesType = gmlFactory.createCoordinatesType();
        coordinatesType.setCs("112.1484375,71.18775391813158 88.59374999999999,70.8446726342528 90,67.87554134672945 96.6796875,65.94647177615738 119.53125,68.9110048456202 112.1484375,71.18775391813158");
        LinearRingType linearRingType = gmlFactory.createLinearRingType();
        linearRingType.setCoordinates(coordinatesType);
        //create Polygon
        PolygonType polygonType = gmlFactory.createPolygonType().withExterior(new AbstractRingPropertyType().withAbstractRing(gmlFactory.createLinearRing(linearRingType)));
        JAXBElement <PolygonType> polygon = gmlFactory.createPolygon(polygonType);
        LocationPropertyType locationPropertyType = new LocationPropertyType();
        locationPropertyType.setAbstractGeometry(polygon);
        JAXBElement <LocationPropertyType> location = gmlFactory.createLocation(locationPropertyType);
        //Feature Member
        // ->Sampling feature
        // -> location
        net.opengis.sampling.v_2_0.ObjectFactory samplingObjectFactory = new net.opengis.sampling.v_2_0.ObjectFactory();
        SFSamplingFeatureType samplingFeatureType = samplingObjectFactory.createSFSamplingFeatureType();
        samplingFeatureType.withLocation(location);
        JAXBElement <SFSamplingFeatureType> samplingFeatureJAXBElement = samplingObjectFactory.createSFSamplingFeature(samplingFeatureType);
        FeatureCollectionType featureCollectionType = new FeatureCollectionType();
        FeaturePropertyType featurePropertyType = new FeaturePropertyType();
        return gmlFactory.createFeatureCollection(featureCollectionType.withFeatureMember(featurePropertyType.withAbstractFeature(samplingFeatureJAXBElement)));
    }



}