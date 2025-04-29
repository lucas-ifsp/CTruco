package com.remotebot;


import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;

import org.springframework.web.reactive.function.client.WebClient;

public class PerformRequestService<T,R> implements HttpRequester<T,R> {
    @Override
    public R post(String path,T content, Class<R> type) throws IOException, InterruptedException {

//        HttpClient client = HttpClient.newHttpClient();
//
//        // Convert the GameIntel object to JSON
//
//        // Build the POST request with JSON content
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(path))
//                .POST(HttpRequest.BodyPublishers.ofString(jsonData, StandardCharsets.UTF_8))
//                .header("Content-Type", "application/json")
//                .build();
//
//        // Send the request and get the response
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//        if (response.statusCode() != 200) {
//            System.out.println("Erro " + response.statusCode() + " ao obter dados da URL " + path);
//            return null;
//        }
//
//        String jsonString = response.body();
//        System.out.println(jsonString);
//        return new Gson().fromJson(jsonString, type);


        WebClient builder = WebClient.builder().build();
        String response = builder
                .post()
                .uri(URI.create(path))
                .header("Content-Type", "application/json")
                .bodyValue(content)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return new Gson().fromJson(response, type);
    }



}
