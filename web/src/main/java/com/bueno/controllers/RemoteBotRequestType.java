package com.bueno.controllers;

import java.util.UUID;

public record RemoteBotRequestType(String name, UUID userId, String url, String port) {// TODO criar dtos no bot UseCase requestModel/responseModel
}
