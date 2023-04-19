package com.caueisa.destroyerbot;

import com.bueno.spi.model.GameIntel;
import com.bueno.spi.service.BotServiceProvider;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class DestroyerBotTest {
  @Mock
  private GameIntel intel;
  private final BotServiceProvider sut = new DestroyerBot();

}