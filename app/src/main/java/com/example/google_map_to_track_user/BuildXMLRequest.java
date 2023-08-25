package com.example.google_map_to_track_user;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class BuildXMLRequest {
    ArrayList<LatLagDO> arlist;
    public static int TIMEOUT_CONNECT_MILLIS = 600000;
    public static int TIMEOUT_READ_MILLIS = 600000;

    public void ConnectionHelper() {



    }

    private final static String SOAP_HEADER = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
            "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
            "<soap:Body><PostEmpLocation xmlns=\"http://tempuri.org/\"><empLocations>";

    private final static String SOAP_FOOTER = " </empLocations></PostEmpLocation></soap:Body>" +
            "</soap:Envelope>";

    static int Response;


    public int HttpSendData(ArrayList<LatLagDO>locationlist) {
        StringBuffer request = new StringBuffer();
        request.append(SOAP_HEADER);
        for (LatLagDO latLagDO : locationlist) {
            try {
                request.append(" <EmpLocation>")
                        .append(" <Id>").append(latLagDO.Id).append(" </Id>")
                        .append("<UID>").append(latLagDO.UID).append("</UID>")
                        .append("<RouteCode>").append(latLagDO.RouteCode).append("</RouteCode>")
                        .append("<UserCode>").append(latLagDO.UserCode).append("</UserCode>")
                        .append("<Latitude>").append(latLagDO.Latitude).append("</Latitude>")
                        .append("<Longitude>").append(latLagDO.Longitude).append("</Longitude>")
                        .append("<CurrentTime>").append(latLagDO.CurrentTime).append("</CurrentTime>")
                        .append("<Attribute1>").append(latLagDO.Attribute1).append("</Attribute1>")
                        .append("<Attribute2>").append(latLagDO.Attribute2).append("</Attribute2>")
                        .append("<Attribute3>").append(latLagDO.Attribute3).append("</Attribute3>")
                        .append("<Attribute4>").append(latLagDO.Attribute4).append("</Attribute4>")
                        .append("<Attribute5>").append(latLagDO.Attribute5).append("</Attribute5>")
                        .append("<Status>").append(latLagDO.Status).append("</Status>")
                        .append("</EmpLocation>");
                Log.d("soapxmlsq", "HttpSendData: "+latLagDO.Id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /*for (int i = 0; i < 100; i++) {
            request.append("<EmpLocation>" +
                    "<Id>" + (i+1) + "</Id>" +
                    "<UID>256-2563-25663</UID>" +
                    "<RouteCode>R99</RouteCode>" +
                    "<UserCode>U1996</UserCode>" +
                    "<Latitude>12.5468</Latitude>" +
                    "<Longitude>20.54688</Longitude>" +
                    "<CurrentTime>2023-01-21 18:28:22:125</CurrentTime>" +
                    "<Attribute1></Attribute1>" +
                    "<Attribute2></Attribute2>" +
                    "<Attribute3></Attribute3>" +
                    "<Attribute4></Attribute4>" +
                    "<Attribute5></Attribute5>" +
                    "<Status>0</Status>" +
                    "</EmpLocation>");
        }*/
        request.append(SOAP_FOOTER);
        Log.d("xmlrequest", "HttpSendData: "+request.toString());

        try {
            String urlParameters = "Id=1012&UID=rd212&RouteCode=c&RouteCode=r9631&Latitude=14.369&Longitude=112.3689";
            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

            String webURL = "http://locationtracker-dev.winitsoftware.com/LocationtrackerServices.asmx";
            URL url = new URL(webURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            String SOAPAction = "http://tempuri.org/PostEmpLocation";

//            connection.setRequestMethod("PATCH");

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "text/xml; charset=utf-8");
            connection.setRequestProperty("SOAPAction", SOAPAction);
            connection.setRequestProperty("Content-Length", Integer.toString(request.length()));
            connection.setRequestProperty("charset", "utf-8");
            connection.setUseCaches(false);

            OutputStream reqStream = connection.getOutputStream();
//            reqStream.write(request.getBytes());
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(reqStream, "UTF-8"));
            bufferedWriter.write(String.valueOf(request));
            bufferedWriter.flush();
            bufferedWriter.close();
            reqStream.close();


            connection.connect();
            int responseCode = connection.getResponseCode();
            String responsemsg = connection.getResponseMessage();
            Log.i("push push", "run: button push" + responseCode + "build" + Thread.currentThread());
            Log.d("responsemsg", "HttpSendData: "+responsemsg);
            return (responseCode);
        } catch (Exception e) {
            e.printStackTrace();
            // Log.d("responsemsg", "HttpSendData: "+e.toString());
        }
        return 201;
    }



}
