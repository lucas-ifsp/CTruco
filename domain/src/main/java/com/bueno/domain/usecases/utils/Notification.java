/*
 *  Copyright (C) 2021 Lucas B. R. de Oliveira - IFSP/SCL
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

package com.bueno.domain.usecases.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Notification {
    private final List<Error> errors = new ArrayList<>();

    public Notification(){
    }

    public Notification(String message) {
        addError(message);
    }

    public Notification(String message, Exception e){
        addError(message, e);
    }

    public void addError(String message){
        addError(message, null);
    }

    public void addError(String message, Exception e){
        errors.add(new Error(message, e));
    }

    public boolean isCorrect(){
        return errors.isEmpty();
    }

    public boolean hasErrors(){
        return !isCorrect();
    }

    private record Error(String message, Exception cause) {}

    public String errorMessage(){
        return errors.stream().map(e -> e.message).collect(Collectors.joining(" ,"));
    }

}
