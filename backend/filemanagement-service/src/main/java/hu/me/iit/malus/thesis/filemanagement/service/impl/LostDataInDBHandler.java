package hu.me.iit.malus.thesis.filemanagement.service.impl;

import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.GET;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LostDataInDBHandler {

    @Value("${google-cloud-bucket-name}")
    private String BUCKET_NAME;

    public void sendGet() throws Exception {
        String urlString = BUCKET_NAME + ".storage.googleapis.com";
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        Map<String, String> parameters = new HashMap<>();
        parameters.put("Host", urlString);
        parameters.put("Date", String.valueOf(new Date()));
        parameters.put("Content-Length", "0");
        parameters.put("Content-Type","text/plain");
        parameters.put("Authorization", "");
    }

}
