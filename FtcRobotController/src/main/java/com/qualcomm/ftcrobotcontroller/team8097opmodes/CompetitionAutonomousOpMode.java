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
    final static int STAGE_INIT_SERVOS = 1;
    final static int STAGE_GO_TO_BEACON = 1;
    final static int STAGE_TURN_TOWARDS_BUTTON = 2;
    final static int STAGE_RAM_WALL = 3;
    final static int STAGE_BACK_UP = 4;
    final static int STAGE_LOOK_FOR_TAPE = 5;
    final static int STAGE_RAM_WALL_AGAIN = 6;
    final static int STAGE_DROP_CLIMBERS = 7;
    final static int STAGE_READ_COLOR = 8;
    final static int STAGE_MOVE_BUTTON_FLAP = 9;
    final static int STAGE_PRESS_BUTTON = 10;
    int stage = STAGE_GO_TO_BEACON;
    ArrayList<Double> distanceToGo = new ArrayList<Double>();
    int distanceToGoIndex = 1;
    boolean stoppedForObstacle = true;
    long startMoveTime;
    long startStoppedTime;
    int colorSensorInputs = 0;
    double blueLightDetected = 0;
    int seesInFront = 0;
    double frontUltraDistance = -1;

    @Override
    public void init() {
        super.init();
        for (int i = 0; i < 10; i++)
            distanceToGo.add(120.0);
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
            } else if (stage == STAGE_RAM_WALL) {
                ramWall();
            } else if (stage == STAGE_BACK_UP) {
                backUp();
            } else if (stage == STAGE_LOOK_FOR_TAPE) {
                lookForTape();
            } else if (stage == STAGE_RAM_WALL_AGAIN) {
                ramWall();
            } else if (stage == STAGE_DROP_CLIMBERS) {
                //dropClimbers();
                endStage();
            } else if (stage == STAGE_READ_COLOR) {
                readColorSensor();
            } else if (stage == STAGE_MOVE_BUTTON_FLAP) {
                moveCorrectButtonFlap();
            } else if (stage == STAGE_PRESS_BUTTON) {
                //pressButton();
                endStage();
            }
        }
    }

    protected void endStage() {
//        printStage("Just finished stage");
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
        } else if (stage == STAGE_LOOK_FOR_TAPE) {
            telemetry.addData(message, "LOOK_FOR_TAPE");
        } else if (stage == STAGE_DROP_CLIMBERS) {
            telemetry.addData(message, "DROP_CLIMBERS");
        } else if (stage == STAGE_READ_COLOR) {
            telemetry.addData(message, "READ_COLOR");
        } else if (stage == STAGE_MOVE_BUTTON_FLAP) {
            telemetry.addData(message, "PRESS_BUTTON");
        }
    }

    protected void initServos() {
        if (System.currentTimeMillis() - startMoveTime < 1000) {
            leftServo.setPosition(leftServoInitPos);
            rightServo.setPosition(rightServoInitPos);
//            armServo.setPosition(0);
        } else {
            endStage();
        }
    }

    protected void goToBeacon() {
        if (frontUltra.getUltrasonicLevel() > 20) {
            seesInFront = 0;
            stoppedForObstacle = false;
            distanceToGo.set(distanceToGoIndex, goDistanceForward(DEFAULT_POWER, distanceToGo.get(distanceToGoIndex - 1), startMoveTime));
            if (distanceToGo.get(distanceToGoIndex) == 0) {
                endStage();
            }
        } else if (seesInFront < 5) {
            seesInFront++;
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
        if (frontUltraDistance == -1) {
            frontUltraDistance = frontUltra.getUltrasonicLevel();
        }
        double distanceToGo = goDistanceForward(DEFAULT_POWER, frontUltraDistance * INCHES_PER_CENT + 4, startMoveTime);
        if (distanceToGo == 0) {
            endStage();
        }
//        if (frontUltra.getUltrasonicLevel() > HITTING_WALL_DISTANCE) {
//            seesInFront = 0;
//            goForward(DEFAULT_POWER / 2.0);
//        } else if (seesInFront < 3) {
//            seesInFront++;
//            goForward(DEFAULT_POWER / 2.0);
//        } else {
//            SystemClock.sleep(100);
//            endStage();
//        }
    }

    protected void backUp() {
        double distanceToGo = goDistanceBackward(DEFAULT_POWER, 10, startMoveTime);
        if (distanceToGo == 0) {
            endStage();
        }
    }

    protected abstract void lookForTape();

    protected abstract void moveCorrectButtonFlap();

    protected void dropClimbers() {
        if (System.currentTimeMillis() - startMoveTime < 1000) {
            armServo.setPosition(1);
        } else {
            armServo.setPosition(0);
            endStage();
        }
    }

    protected void readColorSensor() {
        if (colorSensorInputs < 10) {
            blueLightDetected += colorLightSensor.getLightDetected();
            colorSensorInputs++;
        } else {
            blueLightDetected /= 10.0;
            endStage();
        }
    }

    protected void moveRightFlap() {
        if (System.currentTimeMillis() - startMoveTime < 1000) {
            rightServo.setPosition(rightServoFinalPos);
            leftServo.setPosition(leftServoInitPos);
        } else {
            endStage();
        }
    }

    protected void moveLeftFlap() {
        if (System.currentTimeMillis() - startMoveTime < 1000) {
            leftServo.setPosition(leftServoFinalPos);
            rightServo.setPosition(rightServoInitPos);
        } else {
            endStage();
        }
    }

    protected void pressButton() {
        double distanceToGo = goDistanceForward(DEFAULT_POWER, 20, startMoveTime);
        if (distanceToGo == 0) {
            endStage();
        }
    }
}
