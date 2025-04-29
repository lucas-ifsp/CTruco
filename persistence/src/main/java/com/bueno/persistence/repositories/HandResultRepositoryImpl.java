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

package com.bueno.persistence.repositories;

import com.bueno.domain.usecases.hand.HandResultRepository;
import com.bueno.domain.usecases.hand.dtos.HandResultDto;
import com.bueno.persistence.dao.HandResultDao;
import com.bueno.persistence.dto.HandResultEntity;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;

public class HandResultRepositoryImpl implements HandResultRepository {

    private final HandResultDao dao;

    public HandResultRepositoryImpl(HandResultDao dao) {
        this.dao = dao;
    }

    @Override
    public void save(HandResultDto handResultDto) {
        try {
            dao.save(HandResultEntity.from(handResultDto));
        } catch (SQLException e) {
            System.err.println(e.getClass() + ": " + e.getMessage() + "| hand couldn't be saved");
        }
    }
}
