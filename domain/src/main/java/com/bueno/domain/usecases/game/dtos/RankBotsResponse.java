package com.bueno.domain.usecases.game.dtos;

import java.util.List;

public record RankBotsResponse(boolean isRanking, List<BotRankInfoDto> rank, String message) {
}
