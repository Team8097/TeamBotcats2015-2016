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

import android.os.SystemClock;

import java.util.ArrayList;

public abstract class CompetitionAutonomousOpMode extends AutonomousOpMode {
    final static int STAGE_GO_TO_BEACON = 0;
    final static int STAGE_TURN_TOWARDS_BUTTON = 1;
    final static int STAGE_RAM_WALL = 2;
    final static int STAGE_BACK_UP = 3;
    final static int STAGE_LOOK_FOR_TAPE_RIGHT = 4;
    final static int STAGE_LOOK_FOR_TAPE_LEFT = 5;
    final static int STAGE_DROP_CLIMBERS = 6;
    final static int STAGE_READ_COLOR = 7;
    final static int STAGE_PRESS_BUTTON = 8;
    int stage = STAGE_GO_TO_BEACON;
    ArrayList<Double> distanceToGo = new ArrayList<Double>();
    int distanceToGoIndex = 1;
    boolean stoppedForObstacle = true;
    long startMoveTime;
    long startStoppedTime;
    int colorSensorInputs = 0;
    double redLightDetected = 0;
    boolean foundTape = false;

    @Override
    public void init() {
        super.init();
        armServo.scaleRange(0, 1);
        rightServo.scaleRange(0, 1);
        leftServo.scaleRange(0, 1);
        distanceToGo.add(100.0);
    }

    @Override
    public void loop() {
        if (loop <= initialLoops) {
            loop++;
            armServo.setPosition(0);
            rightServo.setPosition(0);
            leftServo.setPosition(0);
            startMoveTime = System.currentTimeMillis();
        } else {
            if (stage == STAGE_GO_TO_BEACON) {
                goToBeacon();
            } else if (stage == STAGE_TURN_TOWARDS_BUTTON) {
                turnToButton();
            } else if (stage == STAGE_RAM_WALL) {
                ramWall();
            } else if (stage == STAGE_BACK_UP) {
                backUp();
            } else if (stage == STAGE_LOOK_FOR_TAPE_RIGHT) {
                lookForTapeRight();
            } else if (stage == STAGE_LOOK_FOR_TAPE_LEFT) {
                lookForTapeLeft();
            } else if (stage == STAGE_DROP_CLIMBERS) {
                dropClimbers();
            } else if (stage == STAGE_READ_COLOR) {
                readColorSensor();
            } else if (stage == STAGE_PRESS_BUTTON) {
                pressCorrectButton();
            }
        }
    }

    protected void endStage() {
        printStage("Just finished stage");
        stopRobot();
        stage++;
        startMoveTime = System.currentTimeMillis();
    }

    protected void printStage(String message) {
        if (stage == STAGE_GO_TO_BEACON) {
            telemetry.addData(message, "GO_TO_BEACON");
        } else if (stage == STAGE_TURN_TOWARDS_BUTTON) {
            telemetry.addData(message, "TURN_TOWARDS_BUTTON");
        } else if (stage == STAGE_RAM_WALL) {
            telemetry.addData(message, "RAM_WALL");
        } else if (stage == STAGE_BACK_UP) {
            telemetry.addData(message, "BACK_UP");
        } else if (stage == STAGE_LOOK_FOR_TAPE_RIGHT) {
            telemetry.addData(message, "LOOK_FOR_TAPE_RIGHT");
        } else if (stage == STAGE_LOOK_FOR_TAPE_LEFT) {
            telemetry.addData(message, "LOOK_FOR_TAPE_LEFT");
        } else if (stage == STAGE_DROP_CLIMBERS) {
            telemetry.addData(message, "DROP_CLIMBERS");
        } else if (stage == STAGE_READ_COLOR) {
            telemetry.addData(message, "READ_COLOR");
        } else if (stage == STAGE_PRESS_BUTTON) {
            telemetry.addData(message, "PRESS_BUTTON");
        }
    }


    protected void goToBeacon() {
        if (frontUltra.getUltrasonicLevel() > 20) {
            stoppedForObstacle = false;
            distanceToGo.set(distanceToGoIndex, goDistanceForward(DEFAULT_POWER, distanceToGo.get(distanceToGoIndex - 1), startMoveTime));
            if (distanceToGo.get(distanceToGoIndex) == 0) {
                endStage();
            }
        } else if (distanceToGo.get(distanceToGoIndex) < 40) {
            endStage();
        } else {
            if (!stoppedForObstacle) {
                stopRobot();
                stoppedForObstacle = true;
                distanceToGoIndex++;
                startStoppedTime = System.currentTimeMillis();
            }
            if (System.currentTimeMillis() - startStoppedTime > 5000) {
                endStage();
            }
            startMoveTime = System.currentTimeMillis();
        }
        telemetry.addData("Stopped For Obstacle", stoppedForObstacle);
    }

    protected abstract void turnToButton();

    protected void ramWall() {
        double distanceToGo = goDistanceForward(DEFAULT_POWER / 2.0, 40, startMoveTime);
        if (distanceToGo == 0) {
            endStage();
        }
    }

    protected void backUp() {
        double distanceToGo = goDistanceBackward(DEFAULT_POWER, 5, startMoveTime);
        if (distanceToGo == 0) {
            endStage();
        }
    }

    protected void lookForTapeRight() {
        double distanceToGo = goDistanceRight(DEFAULT_POWER, 10, startMoveTime);
        if (frontLightSensor.getLightDetected() > TAPE_THRESHOLD || backLightSensor.getLightDetected() > TAPE_THRESHOLD) {
            foundTape = true;
            telemetry.addData("Found tape on the right", "");
            endStage();
        } else if (distanceToGo == 0) {
            endStage();
        }
    }

    protected void lookForTapeLeft() {
        if (!foundTape) {
            double distanceToGo = goDistanceLeft(DEFAULT_POWER, 20, startMoveTime);
            if (frontLightSensor.getLightDetected() > TAPE_THRESHOLD || backLightSensor.getLightDetected() > TAPE_THRESHOLD) {
                foundTape = true;
                telemetry.addData("Found tape on the left", "");
                endStage();
            } else if (distanceToGo == 0) {
                endStage();
            }
        } else {
            endStage();
        }
    }

    protected abstract void pressCorrectButton();

    protected void dropClimbers() {
        armServo.setPosition(1);
        SystemClock.sleep(1000);
        armServo.setPosition(0);
        SystemClock.sleep(1000);
        endStage();
    }

    protected void readColorSensor() {
        if (colorSensorInputs < 10) {
            redLightDetected += colorLightSensor.getLightDetected();
            colorSensorInputs++;
        } else {
            redLightDetected /= 10.0;
            endStage();
        }
    }

    protected void pressRightButton() {
        rightServo.setPosition(1);
        SystemClock.sleep(1000);
        double distanceToGo = goDistanceForward(DEFAULT_POWER, 20, startMoveTime);
        if (distanceToGo == 0) {
            endStage();
        }
    }

    protected void pressLeftButton() {
        leftServo.setPosition(1);
        SystemClock.sleep(1000);
        double distanceToGo = goDistanceForward(DEFAULT_POWER, 20, startMoveTime);
        if (distanceToGo == 0) {
            endStage();
        }
    }
}
