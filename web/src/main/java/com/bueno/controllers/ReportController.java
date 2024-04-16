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

package com.bueno.controllers;

import com.bueno.domain.usecases.game.usecase.ReportTopWinnersUseCase;
import com.bueno.domain.usecases.game.dtos.TopWinnersDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/reports")
public class ReportController {

    private final ReportTopWinnersUseCase reportTopWinnersUseCase;

    public ReportController(ReportTopWinnersUseCase reportTopWinnersUseCase) {
        this.reportTopWinnersUseCase = reportTopWinnersUseCase;
    }

    @GetMapping(path = "/top-winners/{numberOfTopWinners}")
    public TopWinnersDto topWinners(@PathVariable int numberOfTopWinners){
        return reportTopWinnersUseCase.create(numberOfTopWinners);
    }
}
