package com.bueno.domain.usecases.bot.dtos;

import java.util.UUID;

public record RemoteBotRequestModel(String name, UUID userId, String url,
                                    String port, String repositoryUrl) {
}
