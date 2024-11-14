package com.bueno.domain.usecases.bot.dtos;

import java.util.UUID;

public record TransientRemoteBotDto(UUID uuid,
                                    UUID user,
                                    String name,
                                    String url,
                                    String port,
                                    String repositoryUrl) {
}
