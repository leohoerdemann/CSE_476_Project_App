package msu.edu.cse476.hoerdema.cse476projectapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkUtil {

    private static String BASE_URL = "https://cse-476-project-server-7f7gwdmffa-uc.a.run.app";

    public static void postUserData(String username, int time, String date) {
        // Construct the URL with the provided parameters
        String urlString = String.format(BASE_URL+"/insert/%s/%d/%s", username, time, date);

        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            connection.connect();

            // Read the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
            // Handle IO exceptions
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static double getUserRecords(String username, String date) {
        String urlString = String.format(BASE_URL+"/getrecords/%s/%s", username, date);

        HttpURLConnection connection = null;
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();

            JSONObject json = new JSONObject(result.toString());
            return json.getDouble("Time");

        } catch (IOException e) {
            e.printStackTrace();
            // Handle exceptions appropriately
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return 0;
    }


    public static JSONArray GetAllRecords() {
        String urlString = BASE_URL+"/getrecords";

        HttpURLConnection connection = null;
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream stream = connection.getInputStream();
            InputStreamReader input = new InputStreamReader(stream);

            BufferedReader reader = new BufferedReader(input);
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();


            String jsonString = result.toString();
            return new JSONArray(jsonString);



        } catch (IOException e) {
            e.printStackTrace();
            // Handle exceptions appropriately
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e){
            e.printStackTrace();

            e.getStackTrace();
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    public static JSONArray GetAllRecordsWeekly() {
        String urlString = BASE_URL+"/getweek";

        HttpURLConnection connection = null;
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream stream = connection.getInputStream();
            InputStreamReader input = new InputStreamReader(stream);

            BufferedReader reader = new BufferedReader(input);
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();


            String jsonString = result.toString();
            return new JSONArray(jsonString);



        } catch (IOException e) {
            e.printStackTrace();
            // Handle exceptions appropriately
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e){
            e.printStackTrace();

            e.getStackTrace();
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

}
