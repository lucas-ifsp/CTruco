package com.adivic.octopus;

import com.bueno.spi.model.GameIntel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OctopusTest {
    private Octopus octopus;

    private GameIntel.StepBuilder stepBuilder;

    @BeforeEach
    public void setUp(){
        octopus = new Octopus();
    }
}
