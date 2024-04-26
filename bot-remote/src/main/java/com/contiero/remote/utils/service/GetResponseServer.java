package com.contiero.remote.utils.service;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class GetResponseServer <T> implements GetResponse<T>{
    @Override
    public T getFrom(String path,Class<T> type) throws IOException {

        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            System.out.println("Erro " + conn.getResponseCode() + " ao obter dados da URL " + path);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

        StringBuilder responseBody = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            responseBody.append(line);
        }

        conn.disconnect();

        Gson gson = new Gson();

        return gson.fromJson(String.valueOf(responseBody),type);
    }

}
