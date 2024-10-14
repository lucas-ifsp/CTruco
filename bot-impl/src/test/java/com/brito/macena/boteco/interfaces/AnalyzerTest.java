package com.brito.macena.boteco.interfaces;

import com.brito.macena.boteco.utils.Status;

public class AnalyzerTest {
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