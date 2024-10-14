package com.brito.macena.boteco.interfaces;

import com.brito.macena.boteco.utils.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

public class AnalyzerTest {

    @Nested
    @DisplayName("myHand method tests")
    class MyHandMethodTests {
    }

    private static class AnalyzerImpl extends Analyzer {
        @Override
        public Status threeCardsHandler() {
            return Status.GOOD;
        }

        @Override
        public Status twoCardsHandler() {
            return Status.MEDIUM;
        }

        @Override
        public Status oneCardHandler() {
            return Status.BAD;
        }
    }
}