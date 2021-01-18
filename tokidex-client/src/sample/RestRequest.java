package sample;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import sample.model.Tokimon;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RestRequest {

    private final Gson gson = new Gson();
    private String requestResponseMsg;

    public List<Tokimon> readTokimonFromRemote() {
        requestResponseMsg = "";
        List<Tokimon> tokiomnReceived = new ArrayList<>();
        try{
            URL url = new URL("http://localhost:8080/api/tokimon/all");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String output;
            while((output = br.readLine()) != null){
                sb.append(output);
            }
            System.out.println("Json Received " + sb.toString());
            Type listType = new TypeToken<ArrayList<Tokimon>>(){}.getType();
            tokiomnReceived = gson.fromJson(sb.toString(), listType);
            System.out.println(connection.getResponseCode());
            if(connection.getResponseCode() != 201){
                requestResponseMsg = connection.getResponseCode() + ":" + connection.getResponseMessage();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return tokiomnReceived;
    }

    public void addTokimonRequest(String name, double weight, double height, String type, int strength, String color){
        requestResponseMsg = "";
        try{
            URL url = new URL("http://localhost:8080/api/tokimon/add");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.connect();

            String query = String.format("name=%s&weight=%.1f&height=%.1f&type=%s&strength=%d&color=%s",
                    name, weight, height, type, strength, color);
            System.out.println(query);

            OutputStream os = connection.getOutputStream();
            BufferedWriter br = new BufferedWriter(new OutputStreamWriter(os));

            br.write(query);
            br.flush();
            br.close();
            os.close();

            System.out.println(connection.getResponseCode());
            if(connection.getResponseCode() != 201){
                requestResponseMsg = connection.getResponseCode() + ":" + connection.getResponseMessage();
            }
            connection.disconnect();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteTokimonRequest(long tokimonId) {
        requestResponseMsg = "";
        try{
            URL url = new URL("http://localhost:8080/api/tokimon/" + tokimonId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            connection.connect();
            System.out.println(connection.getResponseCode());
            if(connection.getResponseCode() != 204){
                requestResponseMsg = connection.getResponseCode() + ":" + connection.getResponseMessage();
            }
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editTokimonRequest(long tokimonId, String name, double weight, double height, String type, int strength, String color) {
        requestResponseMsg = "";
        try{
            URL url = new URL("http://localhost:8080/api/tokimon/change/" + tokimonId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.connect();

            String query = String.format("name=%s&weight=%.1f&height=%.1f&type=%s&strength=%d&color=%s",
                    name, weight, height, type, strength, color);

            OutputStream os = connection.getOutputStream();
            BufferedWriter br = new BufferedWriter(new OutputStreamWriter(os));

            br.write(query);
            br.flush();
            br.close();
            os.close();

            System.out.println(connection.getResponseCode());
            if(connection.getResponseCode() != 201){
                requestResponseMsg = connection.getResponseCode() + ":" + connection.getResponseMessage();
            }
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getRequestResponseMsg() {
        return requestResponseMsg;
    }
}
