package com.tcc.laura.sistemamatricula.frontend;

import com.oreilly.servlet.Base64Encoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ApiAuthenticationClient {

    private String baseUrl;
    private String username;
    private String password;
    private String urlResource;
    private String httpMethod;
    private String urlPath;
    private String lastResponse;
    private String payload;
    private HashMap<String, String> parameters;
    private Map<String, List<String>> headerFields;

    public ApiAuthenticationClient(String  baseUrl, String username, String password) {
        setBaseUrl(baseUrl);
        this.username = username;
        this.password = password;
        this.urlResource = "";
        this.urlPath = "";
        this.httpMethod = "GET";
        parameters = new HashMap<>();
        lastResponse = "";
        payload = "";
        headerFields = new HashMap<>();
        // This is important. The application may break without this line.
        System.setProperty("jsse.enableSNIExtension", "false");
    }

    //METODO DESNECESSARIO
    public ApiAuthenticationClient setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        if (!baseUrl.substring(baseUrl.length() - 1).equals("/")) {
            this.baseUrl += "/";
        }
        return this;
    }

    //METODO DESNECESSARIO
    public ApiAuthenticationClient setUrlResource(String urlResource) {
        this.urlResource = urlResource;
        return this;
    }

    //METODO DESNECESSARIO
    public final ApiAuthenticationClient setUrlPath(String urlPath) {
        this.urlPath = urlPath;
        return this;
    }

    public ApiAuthenticationClient setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
        return this;
    }

    public String getLastResponse() {
        return lastResponse;
    }

    public Map<String, List<String>> getHeaderFields() {
        return headerFields;
    }

    public ApiAuthenticationClient setParameters(HashMap<String, String> parameters) {
        this.parameters = parameters;
        return this;
    }

    public ApiAuthenticationClient setParameter(String key, String value) {
        this.parameters.put(key, value);
        return this;
    }

    public ApiAuthenticationClient clearParameters() {
        this.parameters.clear();
        return this;
    }

    public ApiAuthenticationClient removeParameter(String key) {
        this.parameters.remove(key);
        return this;
    }

    public ApiAuthenticationClient clearAll() {
        parameters.clear();
        baseUrl = "";
        this.username = "";
        this.password = "";
        this.urlResource = "";
        this.urlPath = "";
        this.httpMethod = "";
        lastResponse = "";
        payload = "";
        headerFields.clear();
        return this;
    }

    //METODO DESNECESSARIO
    public JSONObject getLastResponseAsJsonObject() {
        try {
            return new JSONObject(String.valueOf(lastResponse));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    //METODO DESNECESSARIO
    public JSONArray getLastResponseAsJsonArray() {
        try {
            return new JSONArray(String.valueOf(lastResponse));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    //METODO DESNECESSARIO
    private String getPayloadAsString() {
        // Cycle through the parameters.
        StringBuilder stringBuffer = new StringBuilder();
        Iterator it = parameters.entrySet().iterator();
        int count = 0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if (count > 0) {
                stringBuffer.append("&");
            }
            stringBuffer.append(pair.getKey()).append("=").append(pair.getValue());

            it.remove(); // avoids a ConcurrentModificationException
            count++;
        }
        return stringBuffer.toString();
    }


    public String execute() {
        String line;
        StringBuilder outputStringBuilder = new StringBuilder();
        System.out.println("ENTREEEEEI0000");
        try {
            StringBuilder urlString = new StringBuilder(baseUrl + urlResource);
            if (!urlPath.equals("")) {
                //urlString.append("/" + urlPath);
            }

            if (parameters.size() > 0 && httpMethod.equals("GET")) {
                payload = getPayloadAsString();
                //urlString.append("?" + payload);
            }

            URL url = new URL(urlString.toString());

            String encoding = Base64Encoder.encode(username + ":" + password);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(httpMethod);
            connection.setRequestProperty("Authorization", "gzip, deflate");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Host", "localhost:8080");
            connection.setRequestProperty("Cache-Control", "no-cache");
            connection.setRequestProperty( "charset", "utf-8");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Connection", "keep-alive");

            // Make the network connection and retrieve the output from the server.
            if (httpMethod.equals("POST") || httpMethod.equals("PUT")) {
                System.out.println(username);
                System.out.println(password);
                payload = "username=" + username + "&password=" + password;
                byte[] postData = payload.getBytes( StandardCharsets.UTF_8 );
                System.out.println(payload);

                //payload = getPayloadAsString();

                connection.setDoInput(true);
                connection.setDoOutput(true);

                try {
                    DataOutputStream writer = new DataOutputStream( connection.getOutputStream());
                    //OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
                    writer.writeBytes(payload);

                    headerFields = connection.getHeaderFields();

                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        System.out.println(line);
                        outputStringBuilder.append(line);
                    }
                } catch (Exception ex) {}
                connection.disconnect();
            }
            else {
                InputStream content = (InputStream) connection.getInputStream();
                headerFields = connection.getHeaderFields();

                //connection.
                BufferedReader in = new BufferedReader(new InputStreamReader(content));

                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                    outputStringBuilder.append(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // If the outputStringBuilder is blank, the call failed.
        if (!outputStringBuilder.toString().equals("")) {
            lastResponse = outputStringBuilder.toString();
        }
        System.out.println(outputStringBuilder.toString());

        if((outputStringBuilder.toString()).contains("code:2")){
            return "granted";
        }
        else if((outputStringBuilder.toString()).contains("code:1")){
            return "notregistered";
        }
        else{
            return "wrong";
        }


        //return outputStringBuilder.toString();
    }
}
