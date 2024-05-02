package com.bueno.domain.usecases.bot.providers.remote;

import com.google.gson.Gson;

import java.net.URI;
import java.util.Arrays;

import com.google.gson.JsonSyntaxException;
import org.springframework.web.reactive.function.client.WebClient;

public class PerformRequestService<T,R> implements HttpRequester<T,R> {
    @Override
    public R post(String path,T content, Class<R> type){
        try {
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
        } catch (JsonSyntaxException e) {
            System.out.println("JsonSyntaxException StackTrace:" + Arrays.toString(e.getStackTrace()));
        }
        catch (Exception e){
            System.out.println("Exception StackTrace: "+ Arrays.toString(e.getStackTrace()));
        }
        return null;
    }

    public R get(String path,Class<R> type){
        try {
            WebClient builder = WebClient.builder().build();
            String response = builder
                    .get()
                    .uri(URI.create(path))
                    .header("Content-Type", "application/json")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return new Gson().fromJson(response, type);
        } catch (JsonSyntaxException e) {
            System.err.println("JsonSyntaxException StackTrace:" + Arrays.toString(e.getStackTrace()));
        }
        catch (Exception e){
            System.err.println("Exception StackTrace: "+ Arrays.toString(e.getStackTrace()));
        }
        return null;
    }



}
