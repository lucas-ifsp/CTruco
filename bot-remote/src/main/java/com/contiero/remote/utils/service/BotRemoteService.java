package com.contiero.remote.utils.service;
/*
 *  Copyright (C) 2022 Lucas B. R. de Oliveira - IFSP/SCL
 *  Contact: lucas <dot> oliveira <at> ifsp <dot> edu <dot> br
 *
 *  This file is part of CTruco (Truco game for didactic purpose).
 *
 *  CTruco is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CTruco is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CTruco.  If not, see <https://www.gnu.org/licenses/>
 */


import com.contiero.remote.utils.model.CardToPlay;
import com.contiero.remote.utils.model.GameIntel;

import java.io.IOException;

public class BotRemoteService {
    private String getMaoDeOnzeResponsePath;
    private String decideIfRaisesPath;
    private String chooseCardPath;
    private String getRaiseResponsePath;
    private String getNamePath;

    public BotRemoteService(String getMaoDeOnzeResponsePath, String decideIfRaisesPath, String chooseCardPath, String getRaiseResponsePath, String getNamePath) {
        this.getMaoDeOnzeResponsePath = getMaoDeOnzeResponsePath;
        this.decideIfRaisesPath = decideIfRaisesPath;
        this.chooseCardPath = chooseCardPath;
        this.getRaiseResponsePath = getRaiseResponsePath;
        this.getNamePath = getNamePath;
    }

    public Boolean getMaoDeOnzeResponse(GameIntel intel) {
        GetResponse<Boolean> getService = new GetResponseServer<>();
        try {
            return getService.getFrom(getMaoDeOnzeResponsePath,Boolean.class);
        } catch (IOException e) {
            System.out.println("Deu ruim no Get Raise Response");
        }
        return null;
    }

    public Boolean decideIfRaises(GameIntel intel) {
        GetResponse<Boolean> getService = new GetResponseServer<>();
        try {
            return getService.getFrom(decideIfRaisesPath,Boolean.class);
        } catch (IOException e) {
            System.out.println("Deu ruim no Get Raise Response");
        }
        return null;
    }

    public CardToPlay chooseCard(GameIntel intel){
        CardToPlay cardToPlay;
        GetResponse<CardToPlay> getService = new GetResponseServer<>();
        try {
             cardToPlay = getService.getFrom(chooseCardPath,CardToPlay.class);
        } catch (IOException e) {
            System.out.println("Deu ruim na seleção de cartas");
            return null;
        }
        return cardToPlay;
    }

    public Integer getRaiseResponse(GameIntel intel) {
        GetResponse<Integer> getService = new GetResponseServer<>();
        try {
            return getService.getFrom(getRaiseResponsePath,Integer.class);
        } catch (IOException e) {
            System.out.println("Deu ruim no Get Raise Response");
        }
        return null;
    }

    public String getName() {
        String name = null;
        GetResponse<String> getService = new GetResponseServer<>();
        try{
            name = getService.getFrom(getNamePath,String.class);
        }
        catch (Exception e){
            System.out.println("DEU ruim pegando  nome");
        }
        return name;
    }
}
