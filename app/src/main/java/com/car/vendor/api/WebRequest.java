package com.car.vendor.api;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by AMAN on 25-10-2015.
 */
public class WebRequest {

        public static JSONObject getData(String url){
            StringBuffer response = new StringBuffer();
            URL obj = null;
            JSONObject jsonObject = null;
            try {
                System.out.println("URL :: " + url);
                obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

                int responseCode = con.getResponseCode();
                System.out.println("GET Response Code :: " + responseCode);
                if (responseCode == HttpURLConnection.HTTP_OK) { // success
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            con.getInputStream()));
                    String inputLine;
                    response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    // print result
                    System.out.println(response.toString());
                    jsonObject = new JSONObject(response.toString());
                } else {
                    System.out.println("GET request not worked");
                }



            } catch (UnsupportedEncodingException e) {
                Log.d("login issue", e.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }catch (JSONException e){
                e.printStackTrace();
            }
            return jsonObject;

        }

    public static JSONObject postData(String data,String url){
        StringBuffer response = new StringBuffer();
        JSONObject jsonObject = null;
        URL obj = null;
        try {
            System.out.println("Data :: " + data);
            System.out.println("URL :: " + url);
            obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            // For POST only - START
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            os.write(data.getBytes("UTF-8"));
            os.flush();
            os.close();
            // For POST only - END
            int responseCode = con.getResponseCode();
            System.out.println("POST Response Code :: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                System.out.println(response.toString());

                jsonObject = new JSONObject(response.toString());

            } else {
                System.out.println("POST request not worked");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }


        return jsonObject;

    }

}
