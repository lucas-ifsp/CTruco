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

package com.bueno.application.utils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class TimelineBuilder {
    private double time;
    private final Timeline timeline;

    public TimelineBuilder() {
        this.timeline = new Timeline();
    }

    public void append(Runnable runnable) {
        append(0.0, runnable);
    }

    public void append(double durationInSeconds, Runnable runnable) {
        time += durationInSeconds;
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(time), e -> runnable.run());
        timeline.getKeyFrames().add(keyFrame);
    }

    public Timeline build() {
        return timeline;
    }

}
