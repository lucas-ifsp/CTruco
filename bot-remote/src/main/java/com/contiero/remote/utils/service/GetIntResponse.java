package com.contiero.remote.utils.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class GetIntResponse implements GetResponse<Integer>{
    @Override
    public Integer getFrom(String path, Class<Integer> type) throws IOException {
        InputStream response = new URL(path).openStream();

        try (Scanner scanner = new Scanner(response)) {
            String responseBody = scanner.useDelimiter("\\A").next();
            return Integer.parseInt(responseBody);
        }
        catch (Exception e){
            System.out.println(e.getStackTrace());
        }
        return null;
    }
}
