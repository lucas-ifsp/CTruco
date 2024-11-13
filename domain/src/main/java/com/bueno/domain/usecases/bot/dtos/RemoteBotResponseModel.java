package com.bueno.domain.usecases.bot.dtos;

public record RemoteBotResponseModel(String botName, String userName, String url, String port,String repositoryUrl) {
}
