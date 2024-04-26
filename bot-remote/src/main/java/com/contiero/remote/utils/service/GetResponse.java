package com.contiero.remote.utils.service;

import java.io.IOException;

public interface GetResponse <T>{
    public T getFrom(String path,Class<T> type) throws IOException;
}
