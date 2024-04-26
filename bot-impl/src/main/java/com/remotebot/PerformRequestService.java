package com.remotebot;


import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class PerformRequestService<T,R> implements HttpRequester<T,R> {
    @Override
    public R post(String path,T content, Class<R> type) throws IOException, InterruptedException {

//        HttpClient client = HttpClient.newHttpClient();
//
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(path))
//                .GET()
//                .header("Accept", "application/json")
//                .build();

        // Create an HTTP client instance
        HttpClient client = HttpClient.newHttpClient();

        // Convert the GameIntel object to JSON
        Gson gson = new Gson();
        String jsonData = gson.toJson(content);

        // Build the POST request with JSON content
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(path))
                .POST(HttpRequest.BodyPublishers.ofString(jsonData, StandardCharsets.UTF_8))
                .header("Content-Type", "application/json")
                .build();

        // Send the request and get the response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            System.out.println("Erro " + response.statusCode() + " ao obter dados da URL " + path);
            return null;
        }

        String jsonString = response.body();
        System.out.println(jsonString);
        return new Gson().fromJson(jsonString, type);
    }



}
