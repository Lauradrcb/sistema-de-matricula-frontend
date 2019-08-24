package com.tcc.laura.sistemamatricula.frontend;

import com.oreilly.servlet.Base64Encoder;

import java.io.DataOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
        this.baseUrl = baseUrl;
        this.username = username;
        this.password = password;
        this.urlResource = "";
        this.urlPath = "";
        this.httpMethod = "GET";
        parameters = new HashMap<>();
        lastResponse = "";
        payload = "";
        headerFields = new HashMap<>();
        // Não remover esta linha
        System.setProperty("jsse.enableSNIExtension", "false");
    }

    public ApiAuthenticationClient setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
        return this;
    }

    public Map<String, List<String>> getHeaderFields() {
        return headerFields;
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

    public String execute() {
        String line;
        StringBuilder outputStringBuilder = new StringBuilder();
        try {
            StringBuilder urlString = new StringBuilder(baseUrl + urlResource);

            URL url = new URL(urlString.toString());

            //App preparada para eventual uso de codificacao
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

            // Prepara os headers da conexao e seta a chamada em formato POST
            if (httpMethod.equals("POST") || httpMethod.equals("PUT")) {
                payload = "username=" + username + "&password=" + password;
                byte[] postData = payload.getBytes( StandardCharsets.UTF_8 );
                connection.setDoInput(true);
                connection.setDoOutput(true);

                try {
                    DataOutputStream writer = new DataOutputStream( connection.getOutputStream());
                    writer.writeBytes(payload);

                    headerFields = connection.getHeaderFields();
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    while ((line = br.readLine()) != null) {
                        outputStringBuilder.append(line);
                    }
                } catch (Exception ex) {}
                connection.disconnect();
            }
            else {
                InputStream content = (InputStream) connection.getInputStream();
                headerFields = connection.getHeaderFields();

                BufferedReader in = new BufferedReader(new InputStreamReader(content));

                while ((line = in.readLine()) != null) {
                    outputStringBuilder.append(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Se o string builder estiver em branco o servidor nao foi encontrado
        if (!outputStringBuilder.toString().equals("")) {
            lastResponse = outputStringBuilder.toString();
        }
        if((outputStringBuilder.toString()).contains("code:2")){
            return "granted, " + outputStringBuilder.toString();
        }
        else if((outputStringBuilder.toString()).contains("code:1")){
            return "notregistered";
        }
        else{
            return "wrong";
        }
    }
}
