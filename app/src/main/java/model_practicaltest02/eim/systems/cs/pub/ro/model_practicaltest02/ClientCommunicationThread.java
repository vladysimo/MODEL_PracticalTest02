package model_practicaltest02.eim.systems.cs.pub.ro.model_practicaltest02;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientCommunicationThread extends Thread {

    private Socket socket = null;

    private String address;
    private String port;
    private String city;
    private String parameters;
    private TextView weatherTextView;

    public ClientCommunicationThread(String address, String port, String city, String parameters, TextView weatherTextView) {
        this.address = address;
        this.port = port;
        this.city = city;
        this.parameters = parameters;
        this.weatherTextView = weatherTextView;
    }

    public void run() {
        if (socket == null) {
            try {
                socket = new Socket(address, Integer.parseInt(port));
                Log.d(Constants.TAG, "[CLIENT] Created communication thread with: " + socket.getInetAddress() + ":" + socket.getLocalPort());
            } catch (UnknownHostException unknownHostException) {
                Log.e(Constants.TAG, "An exception has occurred: " + unknownHostException.getMessage());
                if (Constants.DEBUG) {
                    unknownHostException.printStackTrace();
                }
            } catch (IOException ioException) {
                Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
        }
        try {
            BufferedReader reader = Utilities.getReader(socket);
            PrintWriter writer = Utilities.getWriter(socket);

            if (city == null || city.isEmpty() || parameters == null || parameters.isEmpty()) {
                Log.d(Constants.TAG, "[CLIENT] City or parameters have bad values");
                return;
            }

            writer.println(city);
            writer.flush();
            writer.println(parameters);
            writer.flush();

            String result;
            while ((result = reader.readLine()) != null) {
                final String weatherInfo = result;
                weatherTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        weatherTextView.setText(weatherTextView.getText().toString() + weatherInfo);
                    }
                });
            }

        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
        finally {
            try {
                socket.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}