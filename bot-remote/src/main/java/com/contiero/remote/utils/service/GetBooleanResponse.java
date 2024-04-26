package com.contiero.remote.utils.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class GetBooleanResponse implements GetResponse<Boolean>{
    @Override
    public Boolean getFrom(String path, Class<Boolean> type) throws IOException {
        InputStream response = new URL(path).openStream();

        try (Scanner scanner = new Scanner(response)) {
            String responseBody = scanner.useDelimiter("\\A").next();
            return responseBody.equals("true");
        }
        catch (Exception e){
            System.out.println(e.getStackTrace());
        }
        return null;

    }
}
