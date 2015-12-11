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

/*
 * Base class for opmodes used for autonomous.
 * Includes most of the functionality for autonomous that does not depend on alliance color.
 * Abstract methods in this class have slightly different functions based on alliance color,
 * and are overridden in Red and Blue AutonomousOpMode's.
 */
public abstract class CompetitionAutonomousOpMode extends AutonomousOpMode {
    final static int STAGE_INIT_SERVOS = 0;
    final static int STAGE_GO_TO_OTHER_WALL = 1;
    final static int STAGE_RAM_WALL = 2;
    final static int STAGE_BACK_UP = 3;
    final static int STAGE_LOOK_FOR_TAPE = 4;
    final static int STAGE_ALIGN_WITH_TAPE = 5;
    final static int STAGE_RAM_WALL_AGAIN = 6;
    final static int STAGE_DROP_CLIMBERS = 7;
    final static int STAGE_LIFT_ARM = 8;
    final static int STAGE_READ_COLOR = 9;
    final static int STAGE_PRESS_BUTTON = 10;
    boolean dropClimbers = true;
    int stage = 0;
    double[] distanceToGo = new double[100];
    int distanceToGoIndex = 1;
    boolean stoppedForObstacle = true;
    long startMoveTime;
    long startStoppedTime;
    int colorSensorInputs = 0;
    double blueLightDetected = 0;
    int seesInFront = 0;
    int seesTape = 0;
    boolean frontTape = false;
    boolean backTape = false;

    @Override
    public void init() {
        super.init();
        distanceToGo[0] = 120;
    }

    @Override
    public void loop() {
        if (loop <= initialLoops) {
            loop++;
            startMoveTime = System.currentTimeMillis();
        } else {
            if (stage == STAGE_INIT_SERVOS) {
                initServos();//Sets servos to start positions, as well as enables LEDs for light sensors
            } else if (stage == STAGE_GO_TO_OTHER_WALL) {
                goToOtherWall();//The robot goes to the opposite wall in the alliance zone
            } else if (stage == STAGE_RAM_WALL) {
                ramWall();//The robot hits the wall to align with it
            } else if (stage == STAGE_BACK_UP) {
                backUp();
            } else if (stage == STAGE_LOOK_FOR_TAPE) {
                lookForTape();//The robot moves to the side until it sees tape
            } else if (stage == STAGE_ALIGN_WITH_TAPE) {
                alignWithTape();//The robot turns its front or back wheels until both light sensors see tape
            } else if (stage == STAGE_RAM_WALL_AGAIN) {
                ramWall();
            } else if (stage == STAGE_DROP_CLIMBERS) {
                dropClimbers();//arm controlled by servo moves to drop climbers
            } else if (stage == STAGE_LIFT_ARM) {
                liftArm();//arm is lifted up to let the climbers fall
            } else if (stage == STAGE_READ_COLOR) {
                readColorSensor();//an average of 10 readings or is recorded from the light sensor facing the buttons
            } else if (stage == STAGE_PRESS_BUTTON) {
                moveCorrectButtonFlap();//based on the light detected in the previous stage, the robot presses the correct button
            }
        }
    }

    protected void endStage() {
        stopRobot();
        stage++;
        startMoveTime = System.currentTimeMillis();
    }

    protected void initServos() {
        if (System.currentTimeMillis() - startMoveTime < 1000) {
            leftServo.setPosition(leftServoInitPos);
            rightServo.setPosition(rightServoInitPos);
            armServo.setPosition(armServoInitPos);
            frontLightSensor.enableLed(true);
            backLightSensor.enableLed(true);
        } else {
            endStage();
        }
    }

    protected abstract void goToOtherWall();

    protected void ramWall() {
        double distanceToGo = goDistanceForward(DEFAULT_POWER / 2.0, 16, startMoveTime);
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

    protected abstract void lookForTape();

    protected abstract void alignWithTape();

    protected abstract void moveCorrectButtonFlap();

    protected void dropClimbers() {
        if (dropClimbers) {
            if (System.currentTimeMillis() - startMoveTime < 500) {
                armServo.setPosition(armServoFinalPos);
            } else {
                endStage();
            }
        } else {
            endStage();
        }
    }

    protected void liftArm() {
        if (System.currentTimeMillis() - startMoveTime < 500) {
            armServo.setPosition(armServoInitPos);
        } else {
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
}
