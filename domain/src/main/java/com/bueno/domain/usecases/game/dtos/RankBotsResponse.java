package com.bueno.domain.usecases.game.dtos;

import java.util.List;

public record RankBotsResponse(List<BotRankInfoDto> rank, Long numberOfGames) {
}
