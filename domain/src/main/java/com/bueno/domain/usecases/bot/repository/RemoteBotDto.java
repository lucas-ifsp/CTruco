package com.bueno.domain.usecases.bot.repository;

import com.bueno.domain.usecases.user.dtos.ApplicationUserDto;

import java.util.UUID;

public record RemoteBotDto(UUID uuid, UUID user, String name, String url, String port) { }
