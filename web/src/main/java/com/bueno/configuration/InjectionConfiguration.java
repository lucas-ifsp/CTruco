package com.bueno.configuration;

import com.bueno.domain.usecases.bot.providers.RemoteBotApi;
import com.remote.RemoteBotApiAdapter;
import org.springframework.context.annotation.*;

@Configuration
public class InjectionConfiguration {
    @Bean
    public RemoteBotApi getRemoteBotApi() {
        return new RemoteBotApiAdapter();
    }
}
