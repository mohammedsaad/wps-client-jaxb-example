package com.ogcxml.wps.cleint.example;

import net.opengis.gml.v_3_2_1.FeatureCollectionType;
import org.n52.geoprocessing.wps.client.ExecuteRequestBuilder;
import org.n52.geoprocessing.wps.client.WPSClientException;
import org.n52.geoprocessing.wps.client.WPSClientSession;
import org.n52.geoprocessing.wps.client.model.Process;
import org.n52.geoprocessing.wps.client.model.ResponseMode;
import org.n52.geoprocessing.wps.client.model.Result;
import org.n52.geoprocessing.wps.client.model.execution.Execute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.io.IOException;

public class WpsClientApplication {
    private static final Logger log = LoggerFactory.getLogger(WpsClientApplication.class);
    private static final String WPS_URL = "http://geoprocessing.demo.52north.org/javaps/service";

    private final FeatureCollectionBuilder featureCollectionBuilder = new FeatureCollectionBuilder();

    public static void main(String[] args) throws Exception {

        WpsClientApplication application = new WpsClientApplication();
        application.executeEchoProcess();

    }


    public void executeEchoProcess() throws Exception {
        JAXBElement <FeatureCollectionType> XmlObject = featureCollectionBuilder.createFeatureCollection();
        Marshaller marshaller = new Marshaller();

        WPSClientSession session = new WPSClientSession();
        session.connect(WPS_URL, "2.0.0");

        Process desc = session.getProcessDescription(WPS_URL, "org.n52.javaps.test.EchoProcess", "2.0.0");

        Result response = prepareAndExecute(marshaller.marshal(XmlObject), session, desc);

        Object result = response.getOutputs().stream().map(o -> o.asComplexData().getValue()).findFirst().orElseThrow(()-> new Exception("no data"));
        log.info("Result: {}", result);
    }


    private Result prepareAndExecute(String input, WPSClientSession session, Process processDescription) throws WPSClientException, IOException, JAXBException {
        return prepareAndExecute(input, session, processDescription, false);
    }


    private Result prepareAndExecute(String input, WPSClientSession session, Process processDescription, boolean outputAsReference) throws WPSClientException, IOException {
        ExecuteRequestBuilder builder = new ExecuteRequestBuilder(processDescription);

        builder.addComplexData("complexInput", input, "http://schemas.opengis.net/gml/3.2.1/base/feature.xsd", null, "text/xml");
        builder.addOutput("complexOutput", "http://schemas.opengis.net/gml/3.2.1/base/feature.xsd", null, "text/xml");

        if (outputAsReference) {
            builder.setAsReference("complexOutput", true);
        }

        Execute executeRequest = builder.getExecute();
        executeRequest.setResponseMode(ResponseMode.DOCUMENT);

        return (Result) session.execute(WPS_URL, executeRequest, "2.0.0");
    }


}


