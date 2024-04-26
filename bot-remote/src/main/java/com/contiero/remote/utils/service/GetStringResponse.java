package com.contiero.remote.utils.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class GetStringResponse implements GetResponse<String>{

    @Override
    public String getFrom(String path,Class<String> type) throws IOException {
        InputStream response = new URL(path).openStream();

        try (Scanner scanner = new Scanner(response)) {
            String responseBody = scanner.useDelimiter("\\A").next();
            System.out.println(responseBody);
        }
        catch (Exception e){
            System.out.println(e.getStackTrace());
        }
        return null;
    }
}
