package com.abdullahhussein.pushnotifications;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class PushNotifications {

    static OnNotificationReceivedListener mOnNotificationReceivedListener;

    public static void initializesFCM(String token, OnNotificationReceivedListener onNotificationReceivedListener) {

        mOnNotificationReceivedListener = onNotificationReceivedListener;

        sendToken(token);
    }

    static void sendToken(final String token) {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    String command = "http://api.pushbots.com/2/subscriptions";

                    URL url = new URL(command);

                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    try {
                        urlConnection.setDoInput(true);
                        urlConnection.setDoOutput(true);
                        urlConnection.setChunkedStreamingMode(0);
                        urlConnection.setRequestMethod("POST");

                        urlConnection.setConnectTimeout(10000);
                        urlConnection.setReadTimeout(10000);

                        urlConnection.setRequestProperty("Content-Type", "application/json");
                        urlConnection.setRequestProperty("x-pushbots-appid", "5d258e58b7941208c73fcfb7");

                        JSONObject params = new JSONObject();
                        params.put("platform", 1);
                        params.put("token", token);

                        //Request
                        OutputStream os = urlConnection.getOutputStream();
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
                        writer.write(params.toString());
                        writer.flush();
                        writer.close();
                        os.close();
                        urlConnection.connect();

                        if (urlConnection.getResponseCode() >= 400) {
                            Log.e("Error", "http response code is " + urlConnection.getResponseCode());
                            return null;
                        }

                        InputStream is = urlConnection.getInputStream();
                        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                        String line;
                        StringBuilder response = new StringBuilder();
                        while ((line = rd.readLine()) != null) {
                            response.append(line);
                        }
                        rd.close();
                        Log.e("Error", "Result : " + response.toString() + "\n");
                    } finally {
                        urlConnection.disconnect();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }

        }.execute();

    }
}
