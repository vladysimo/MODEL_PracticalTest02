package model_practicaltest02.eim.systems.cs.pub.ro.model_practicaltest02;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class ServerCommunicationThread extends Thread {

    private Socket socket;
    private ServerThread serverThread;

    public ServerCommunicationThread(Socket socket, ServerThread serverThread) {
        this.serverThread = serverThread;
        if (socket != null) {
            this.socket = socket;
            Log.d(Constants.TAG, "[COMM SERVER] Created communication thread with: " + socket.getInetAddress() + ":" + socket.getLocalPort());
        }
    }

    public void run() {
        try {
            BufferedReader reader = Utilities.getReader(socket);
            PrintWriter writer = Utilities.getWriter(socket);

            String city = reader.readLine();
            String informationType = reader.readLine();
            if (city == null || city.isEmpty() || informationType == null || informationType.isEmpty()) {
                Log.d(Constants.TAG, "[COMM SERVER] Bad city and information type");
                return;
            }

            Map<String, WeatherInformation> data = serverThread.getData();
            WeatherInformation info = null;
            if (data.containsKey(city)) {
                Log.d(Constants.TAG, "[COMM SERVER] City information was cached");
                info = data.get(city);
            }
            else {
                Log.d(Constants.TAG, "[COMM SERVER] Getting information");
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(Constants.API_SERVER + city + ".json");
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                String pageSourceCode = httpClient.execute(httpGet, responseHandler);

                JSONObject content = new JSONObject(pageSourceCode);
                JSONObject currentObservation = content.getJSONObject("current_observation");
                String temperature = currentObservation.getString("temp_c");
                String windSpeed = currentObservation.getString("wind_string");
                String pressure = currentObservation.getString("pressure_mb");
                String humidity = currentObservation.getString("relative_humidity");
                info = new WeatherInformation(
                        temperature, windSpeed, pressure, humidity
                );
                serverThread.setData(city, info);
            }

            if (info == null) {
                Log.d(Constants.TAG, "[COMM SERVER] No city information available");
                return;
            }

            String result;
            if (informationType.equals("all")) {
                result = info.toString();
            }
            else if (informationType.equals("temperature")) {
                result = info.temperature;
            }
            else  if (informationType.equals("wind_speed")) {
                result = info.windSpeed;
            }
            else if (informationType.equals("humidity")) {
                result = info.humidity;
            }
            else if (informationType.equals("pressure")) {
                result = info.pressure;
            }
            else {
                Log.d(Constants.TAG, "[COMM SERVER] Wrong information type");
                return;
            }

            writer.println(result);
            writer.flush();

        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        finally {
            if (socket != null) {
                try {
                    socket.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
