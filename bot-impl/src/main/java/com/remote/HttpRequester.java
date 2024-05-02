package com.remote;

public interface HttpRequester<T,R>{
    R post(String path,T content, Class<R> type);

    R get(String path,Class<R> type);
}
