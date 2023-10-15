package com.casal.impl.vapobot;

import com.cremonezzi.impl.carlsenbot.Carlsen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VapoBotTest {
    private VapoBot vapoBot;

    @BeforeEach
    public void config() {
        vapoBot = new VapoBot();
    }
}