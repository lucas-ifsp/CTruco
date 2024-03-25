package com.bueno.application.withbots.commands;

import com.bueno.application.utils.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RankPrinter implements Command<Void>{

    private final Map<String,Long> rankMap;

    public RankPrinter(Map<String, Long> rankMap) {
        this.rankMap = rankMap;
    }

    @Override
    public Void execute() {
        List<String> winsOfEachBot = new ArrayList<>(rankMap.keySet());
        int i = 1;
        while (!winsOfEachBot.isEmpty()){
            var maxValue = winsOfEachBot.stream().map(this::keyToLong).max(this::compare);
            System.out.println("["+i+"] "+winsOfEachBot);
        }
        return null;
    }

    private int compare(Long o1, Long o2) {
        if (o1.equals(o2)){
            return 0;
        }
        if (o1>o2){
            return 1;
        }
        return -1;
    }

    private Long keyToLong(String key){
        return rankMap.get(key);
    }
}
