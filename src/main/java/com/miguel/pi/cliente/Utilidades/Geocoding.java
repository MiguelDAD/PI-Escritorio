package com.miguel.pi.cliente.Utilidades;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Miguel
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import org.json.JSONArray;
import org.json.JSONObject;

public class Geocoding {

    public static String LatitudLongitud(String direccion) {
        String clave = "AIzaSyAFnPuAFb67bdkvZ6YDzqEggWVPHcQMCgA";

        try {
            String direccionCodificada = URLEncoder.encode(direccion, "UTF-8");
            String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + direccionCodificada + "&key=" + clave;

            URL urlObj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject jsonObject = new JSONObject(response.toString());
            String status = jsonObject.getString("status");
            if (status.equals("OK")) {
                JSONArray resultados = jsonObject.getJSONArray("results");
                JSONObject primerResultado = resultados.getJSONObject(0);
                JSONObject ubicacion = primerResultado.getJSONObject("geometry").getJSONObject("location");
                double latitud = ubicacion.getDouble("lat");
                double longitud = ubicacion.getDouble("lng");

                System.out.println("Latitud: " + latitud);
                System.out.println("Longitud: " + longitud);
                return latitud + "," + longitud;
            } else {
                System.out.println("No se encontraron resultados para la dirección proporcionada.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";

    }

    public static String obtenerNombre(String latitud, String longitud) {
       String clave = "AIzaSyAFnPuAFb67bdkvZ6YDzqEggWVPHcQMCgA";

        try {
            String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitud + "," + longitud + "&key=" + clave;

            URL urlObj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject jsonObject = new JSONObject(response.toString());
            String status = jsonObject.getString("status");
            if (status.equals("OK")) {
                JSONArray resultados = jsonObject.getJSONArray("results");
                JSONObject primerResultado = resultados.getJSONObject(0);
                String nombreUbicacion = primerResultado.getString("formatted_address");

                //System.out.println("Nombre de la ubicación: " + nombreUbicacion);
                return nombreUbicacion;
            } else {
                System.out.println("No se encontraron resultados para la ubicación proporcionada.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";

    }

}
