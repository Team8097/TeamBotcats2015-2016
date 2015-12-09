/* Copyright (c) 2014 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.qualcomm.ftcrobotcontroller.team8097opmodes;

import java.util.ArrayList;

public abstract class CompetitionAutonomousOpMode extends AutonomousOpMode {
    final static int STAGE_GO_TO_BEACON = 0;
    final static int STAGE_TURN_TOWARDS_BUTTON = 1;
    final static int STAGE_ALIGN_WITH_TAPE = 2;
    final static int STAGE_DROP_CLIMBERS = 3;
    final static int STAGE_READ_COLOR = 4;
    final static int STAGE_PRESS_BUTTON = 5;
    int stage = STAGE_GO_TO_BEACON;
    ArrayList<Double> distanceToGo = new ArrayList<Double>();
    int distanceToGoIndex = 1;
    boolean stoppedForObstacle = true;
    long startMoveTime;
    long startStoppedTime;
    int colorSensorInputs = 0;
    double colorSensorRed = 0;
    double colorSensorBlue = 0;
    int color;

    @Override
    public void init() {
        super.init();
        distanceToGo.add(100.0);
    }

    @Override
    public void loop() {
        if (loop <= initialLoops) {
            loop++;
            startMoveTime = System.currentTimeMillis();
        } else {
            if (stage == STAGE_GO_TO_BEACON) {
                goToBeacon();
            } else if (stage == STAGE_TURN_TOWARDS_BUTTON) {
                turnToButton();
            } else if (stage == STAGE_ALIGN_WITH_TAPE) {
                alignWithTape();
            } else if (stage == STAGE_DROP_CLIMBERS) {
                dropClimbers();
            } else if (stage == STAGE_READ_COLOR) {
                readColorSensor();
            } else if (stage == STAGE_PRESS_BUTTON) {
                pressCorrectButton();
            }
        }
    }


    protected void goToBeacon() {
        if (frontUltra.getUltrasonicLevel() > 20) {
            stoppedForObstacle = false;
            distanceToGo.set(distanceToGoIndex, goDistance(defaultPower, distanceToGo.get(distanceToGoIndex - 1), startMoveTime));
            if (distanceToGo.get(distanceToGoIndex) == 0) {
                stopRobot();
                stage++;
                startMoveTime = System.currentTimeMillis();
            }
        } else if (distanceToGo.get(distanceToGoIndex) < 40) {
            stopRobot();
            stage++;
            startMoveTime = System.currentTimeMillis();
        } else {
            if (!stoppedForObstacle) {
                stopRobot();
                stoppedForObstacle = true;
                distanceToGoIndex++;
                startStoppedTime = System.currentTimeMillis();
            }
            if (System.currentTimeMillis() - startStoppedTime > 7000) {
                stage++;
                startMoveTime = System.currentTimeMillis();
            }
            startMoveTime = System.currentTimeMillis();
        }
    }

    protected abstract void turnToButton();

    protected void alignWithTape() {

    }

    protected abstract void pressCorrectButton();

    protected void dropClimbers() {
        if (1 - rightServo.getPosition() > 0.1) {
            rightServo.setPosition(1);
        } else {
            rightServo.setPosition(0);
            stage++;
        }
    }

    protected void readColorSensor() {
        if (colorSensorInputs < 10) {
            colorSensorRed += colorSensor.red();
            colorSensorBlue += colorSensor.blue();
            colorSensorInputs++;
        } else {
            stage++;
        }
    }

    protected void pressRightButton() {
        rightServo.setPosition(1);
    }

    protected void pressLeftButton() {
        leftServo.setPosition(1);
    }
}
