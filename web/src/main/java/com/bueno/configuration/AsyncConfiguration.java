package com.bueno.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;


@Configuration
@EnableAsync
public class AsyncConfiguration {
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // Ajuste o número de threads principais e máximas conforme necessário
        executor.setCorePoolSize(10);            // Número mínimo de threads
        executor.setMaxPoolSize(20);             // Número máximo de threads
        executor.setQueueCapacity(500);          // Capacidade da fila
        executor.setKeepAliveSeconds(60);        // Tempo de vida das threads extras

        // Configuração opcional para rastreamento
        executor.setThreadNamePrefix("Simulation-");

        // RejectionPolicy: o que fazer se todas as threads e a fila estão cheias
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        executor.initialize();
        return executor;
    }
}
