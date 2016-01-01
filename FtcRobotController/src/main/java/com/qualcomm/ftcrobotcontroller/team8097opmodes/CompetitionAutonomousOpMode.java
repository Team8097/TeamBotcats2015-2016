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

import com.qualcomm.ftcrobotcontroller.FtcRobotControllerActivity;

/*
 * Base class for opmodes used for autonomous.
 * Includes most of the functionality for autonomous that does not depend on alliance color.
 * Abstract methods in this class have slightly different functions based on alliance color,
 * and are overridden in Red and Blue AutonomousOpMode's.
 */
public abstract class CompetitionAutonomousOpMode extends AutonomousOpMode {
    final static int STAGE_INIT_SERVOS = 0;
    final static int STAGE_MAKE_TRIANGLE = 1;
    final static int STAGE_GO_TO_OTHER_WALL = 2;
    final static int STAGE_ALIGN_WITH_WALL = 3;
    final static int STAGE_LOOK_FOR_TAPE = 4;
    final static int STAGE_ALIGN_WITH_TAPE = 5;
    final static int STAGE_BACK_UP = 6;
    final static int STAGE_ALIGN_WITH_WALL_2 = 7;
    final static int STAGE_OPEN_SWEEPERS = 8;
    final static int STAGE_CLOSE_SWEEPERS = 9;
    final static int STAGE_RAM_WALL = 10;
    final static int STAGE_DROP_CLIMBERS = 11;
    final static int STAGE_LIFT_ARM = 12;
    final static int STAGE_READ_COLOR = 13;
    final static int STAGE_PRESS_BUTTON = 14;
    boolean dropClimbers = true;
    int stage = 0;
    int sweepStage = 0;
    double[] distanceToGo = new double[100];
    int distanceToGoIndex = 1;
    boolean stoppedForObstacle = true;
    long startMoveTime;
    long startStoppedTime;
    int colorSensorInputs = 0;
    double rightLightDetected = 0;
    double leftLightDetected = 0;
    int seesWallLeft = 0;
    int seesWallRight = 0;
    int ultraInputs = 0;
    int seesTape = 0;
    boolean frontTape = false;
    boolean backTape = false;
    double frontTapeThreshold;
    double backTapeThreshold;

    @Override
    public void init() {
        super.init();
        distanceToGo[0] = 120;
        frontTapeThreshold = (FtcRobotControllerActivity.calibrationSP.getFloat("frontTapeValue", -2) + FtcRobotControllerActivity.calibrationSP.getFloat("frontGroundValue", -2)) / 2.0;
        if (frontTapeThreshold < 0) {
            frontTapeThreshold = 0.43;
        }
        backTapeThreshold = (FtcRobotControllerActivity.calibrationSP.getFloat("backTapeValue", -2) + FtcRobotControllerActivity.calibrationSP.getFloat("backGroundValue", -2)) / 2.0;
        if (backTapeThreshold < 0) {
            backTapeThreshold = 0.43;
        }

    }

    @Override
    public void loop() {
        if (loop <= initialLoops) {
            loop++;
            startMoveTime = System.currentTimeMillis();
        } else {
            if (stage == STAGE_INIT_SERVOS) {
                initServos();//Sets servos to start positions, as well as enables LEDs for light sensors
            } else if (stage == STAGE_MAKE_TRIANGLE) {
                sweepTriangle();
            } else if (stage == STAGE_GO_TO_OTHER_WALL) {
                goToOtherWall();//The robot goes to the opposite wall in the alliance zone
            } else if (stage == STAGE_ALIGN_WITH_WALL) {
                alignWithWall();
            } else if (stage == STAGE_LOOK_FOR_TAPE) {
                lookForTape();//The robot moves to the side until it sees tape
            } else if (stage == STAGE_ALIGN_WITH_TAPE) {
                alignWithTape();//The robot turns its front or back wheels until both light sensors see tape
            } else if (stage == STAGE_BACK_UP) {
                backUp();
            } else if (stage == STAGE_ALIGN_WITH_WALL_2) {
                alignWithWall();
            } else if (stage == STAGE_OPEN_SWEEPERS) {
                openSweepers();
            } else if (stage == STAGE_CLOSE_SWEEPERS) {
                closeSweepers();
            } else if (stage == STAGE_RAM_WALL) {
                ramWall();//The robot hits the wall to align with it
            } else if (stage == STAGE_DROP_CLIMBERS) {
                dropClimbers();//arm controlled by servo moves to drop climbers
            } else if (stage == STAGE_LIFT_ARM) {
                liftArm();//arm is lifted up to let the climbers fall
            } else if (stage == STAGE_READ_COLOR) {
                readColorSensor();//an average of 10 readings or is recorded from the light sensor facing the buttons
            } else if (stage == STAGE_PRESS_BUTTON) {
                moveCorrectButtonFlap();//based on the light detected in the previous stage,
                // the robot presses the correct button
            }
        }
    }

    protected void endStage() {
        stopRobot();
        stage++;
        startMoveTime = System.currentTimeMillis();
    }

    protected void initServos() {
        if (System.currentTimeMillis() - startMoveTime < 300) {
            leftServo.setPosition(leftServoInitPos);
            rightServo.setPosition(rightServoInitPos);
            armServo.setPosition(armServoInitPos);
            leftSweepServo.setPosition(leftSweepIn);
            rightSweepServo.setPosition(rightSweepIn);
            frontLightSensor.enableLed(true);
            backLightSensor.enableLed(true);
        } else {
            endStage();
        }
    }

    protected void sweepTriangle() {
        final int rightOut = 0;
        final int leftTriangle = 1;
        final int rightTriangle = 2;
        if (sweepStage == rightOut) {
            if (System.currentTimeMillis() - startMoveTime < 300) {
                rightSweepServo.setPosition(rightSweepOut);
            } else {
                sweepStage++;
                startMoveTime = System.currentTimeMillis();
            }
        } else if (sweepStage == leftTriangle) {
            if (System.currentTimeMillis() - startMoveTime < 300) {
                leftSweepServo.setPosition(leftSweepTriangle);
            } else {
                sweepStage++;
                startMoveTime = System.currentTimeMillis();
            }
        } else if (sweepStage == rightTriangle) {
            if (System.currentTimeMillis() - startMoveTime < 300) {
                rightSweepServo.setPosition(rightSweepTriangle);
            } else {
                sweepStage++;
                startMoveTime = System.currentTimeMillis();
            }
        } else {
            sweepStage = 0;
            endStage();
        }
    }

    protected void goToOtherWall() {
        if (frontLeftUltra.getUltrasonicLevel() > 30 || frontRightUltra.getUltrasonicLevel() > 30) {
            seesWallLeft = 0;
            seesWallRight = 0;
            stoppedForObstacle = false;
            distanceToGo[distanceToGoIndex] = goDirectionOfOtherWall(DEFAULT_POWER, distanceToGo[distanceToGoIndex - 1], startMoveTime);
        } else if (seesWallLeft < 15 || seesWallRight < 15) {
            seesWallLeft++;
            seesWallRight++;
            stoppedForObstacle = false;
            distanceToGo[distanceToGoIndex] = goDirectionOfOtherWall(DEFAULT_POWER, distanceToGo[distanceToGoIndex - 1], startMoveTime);
        } else if (distanceToGo[distanceToGoIndex] < 36) {
            seesWallLeft = 0;
            seesWallRight = 0;
            endStage();
        } else {
            stopRobot();
            if (!stoppedForObstacle) {
                stoppedForObstacle = true;
                distanceToGoIndex++;
                startStoppedTime = System.currentTimeMillis();
            }
            if (System.currentTimeMillis() - startStoppedTime > 5000) {
                dropClimbers = false;//There is something wrong, so better save dropping climbers for TeleOp to be safe
                seesWallLeft = 0;
                seesWallRight = 0;
                endStage();
            }
            startMoveTime = System.currentTimeMillis();
        }
    }

    protected abstract double goDirectionOfOtherWall(double power, double inches, long startTime);

    protected void alignWithWall() {
        if (ultraInputs < 20) {
            stopRobot();
            if (frontLeftUltra.getUltrasonicLevel() <= LEFT_ULTRA_PERFECT_DIST) {
                seesWallLeft++;
            } else if (seesWallLeft < 10) {
                seesWallLeft = 0;
            }
            if (frontRightUltra.getUltrasonicLevel() <= RIGHT_ULTRA_PERFECT_DIST) {
                seesWallLeft++;
            } else if (seesWallLeft < 10) {
                seesWallLeft = 0;
            }
            ultraInputs++;
            startMoveTime = System.currentTimeMillis();
        } else {
            if (seesWallLeft < 10 && seesWallRight < 10) {
                double distanceToGo = goDistanceForward(DEFAULT_POWER / 2.0, 1 * INCHES_PER_CENT, startMoveTime);
                if (distanceToGo == 0) {
                    ultraInputs = 0;
                }
            } else if (seesWallRight < 10) {
                double distanceToGo = goDistanceRightWheelsForward(DEFAULT_POWER / 2.0, 1 * INCHES_PER_CENT, startMoveTime);
                if (distanceToGo == 0) {
                    ultraInputs = 0;
                }
            } else if (seesWallLeft < 10) {
                double distanceToGo = goDistanceLeftWheelsForward(DEFAULT_POWER / 2.0, 1 * INCHES_PER_CENT, startMoveTime);
                if (distanceToGo == 0) {
                    ultraInputs = 0;
                }
            } else {
                endStage();
            }
        }
    }

    protected void openSweepers() {
        if (System.currentTimeMillis() - startMoveTime < 300) {
            rightSweepServo.setPosition(rightSweepOut);
            leftSweepServo.setPosition(leftSweepOut);
        } else {
            endStage();
        }
    }

    protected void closeSweepers() {
        final int leftIn = 0;
        final int rightIn = 1;
        if (sweepStage == leftIn) {
            if (System.currentTimeMillis() - startMoveTime < 300) {
                leftSweepServo.setPosition(leftSweepIn);
            } else {
                sweepStage++;
                startMoveTime = System.currentTimeMillis();
            }
        } else if (sweepStage == rightIn) {
            if (System.currentTimeMillis() - startMoveTime < 300) {
                rightSweepServo.setPosition(rightSweepIn);
            } else {
                sweepStage++;
                startMoveTime = System.currentTimeMillis();
            }
        } else {
            sweepStage = 0;
            endStage();
        }
    }

    protected void ramWall() {
        double distanceToGo = goDistanceForward(DEFAULT_POWER / 2.0, 16, startMoveTime);
        if (distanceToGo == 0) {
            endStage();
        }
    }

    protected void backUp() {
        double distanceToGo = goDistanceBackward(DEFAULT_POWER, 2.5, startMoveTime);
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
            rightLightDetected += rightColorSensor.getLightDetected();
            leftLightDetected += leftColorSensor.getLightDetected();
            colorSensorInputs++;
        } else {
            rightLightDetected /= 10.0;
            leftLightDetected /= 10.0;
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
