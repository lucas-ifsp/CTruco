package com.remotebot;

import java.io.IOException;

public interface HttpRequester<T,R>{
    R post(String path,T content, Class<R> type) throws IOException, InterruptedException;
}
