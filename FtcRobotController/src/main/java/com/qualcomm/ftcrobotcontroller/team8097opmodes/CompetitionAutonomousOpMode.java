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
    final static int STAGE_GO_TO_OTHER_WALL = 1;
    final static int STAGE_ALIGN_WITH_WALL = 2;
    final static int STAGE_LOOK_FOR_TAPE = 3;
    final static int STAGE_ALIGN_WITH_TAPE = 4;
    final static int STAGE_ALIGN_WITH_TAPE_PERFECT = 5;
    final static int STAGE_BACK_UP = 6;
    final static int STAGE_ALIGN_WITH_WALL_2 = 7;
    final static int STAGE_PUT_DOWN_FLAPS = 8;
    final static int STAGE_READ_COLOR = 9;
    final static int STAGE_MOVE_BUTTON_FLAP = 10;
    final static int STAGE_APPROACH_BUTTON = 11;
    final static int STAGE_RAM_BUTTON = 12;
    final static int STAGE_DROP_CLIMBERS = 13;
    final static int STAGE_LIFT_ARM = 14;
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
    int seesOpeningLeft = 0;
    int seesOpeningRight = 0;
    int sensorInputs = 0;
    int seesTape = 0;
    int seesTapeFront = 0;
    int seesTapeBack = 0;
    boolean frontTape = false;
    boolean backTape = false;
    double frontTapeLowThreshold;
    double backTapeLowThreshold;
    double frontTapeHighThreshold;
    double backTapeHighThreshold;
    long startProgramTime;
    boolean fail = false;
    int alignPeriods = 0;

    @Override
    public void init() {
        super.init();
        distanceToGo[0] = 120;
        frontTapeLowThreshold = (FtcRobotControllerActivity.calibrationSP.getFloat("frontTapeValue", -2) + FtcRobotControllerActivity.calibrationSP.getFloat("frontGroundValue", -2)) / 2.0;
        if (frontTapeLowThreshold < 0) {
            frontTapeLowThreshold = 0.43;
        }
        backTapeLowThreshold = (FtcRobotControllerActivity.calibrationSP.getFloat("backTapeValue", -2) + FtcRobotControllerActivity.calibrationSP.getFloat("backGroundValue", -2)) / 2.0;
        if (backTapeLowThreshold < 0) {
            backTapeLowThreshold = 0.43;
        }
        frontTapeHighThreshold = (FtcRobotControllerActivity.calibrationSP.getFloat("frontTapeValue", -2) + FtcRobotControllerActivity.calibrationSP.getFloat("frontRedTapeValue", -2)) / 2.0;
        if (frontTapeHighThreshold < 0) {
            frontTapeHighThreshold = 0.62;
        } else if (FtcRobotControllerActivity.calibrationSP.getFloat("frontRedTapeValue", -2) < FtcRobotControllerActivity.calibrationSP.getFloat("frontTapeValue", -2)) {
            frontTapeHighThreshold = 2;
        }
        backTapeHighThreshold = (FtcRobotControllerActivity.calibrationSP.getFloat("backTapeValue", -2) + FtcRobotControllerActivity.calibrationSP.getFloat("backRedTapeValue", -2)) / 2.0;
        if (backTapeHighThreshold < 0) {
            backTapeHighThreshold = 0.62;
        } else if (FtcRobotControllerActivity.calibrationSP.getFloat("backRedTapeValue", -2) < FtcRobotControllerActivity.calibrationSP.getFloat("backTapeValue", -2)) {
            backTapeHighThreshold = 2;
        }
    }

    @Override
    public void loop() {
        if (loop <= initialLoops) {
            loop++;
            startMoveTime = System.currentTimeMillis();
            startProgramTime = System.currentTimeMillis();
        } else {
            if (stage == STAGE_INIT_SERVOS) {
                initServos();//Sets servos to start positions, as well as enables LEDs for light sensors
            } else if (stage == STAGE_GO_TO_OTHER_WALL) {
                goToOtherWall();//The robot goes to the opposite wall in the alliance zone
            } else if (stage == STAGE_ALIGN_WITH_WALL) {
//                if (!fail) {
//                    alignWithWall(15);
//                } else {
//                    endStage();
//                }
                endStage();
            } else if (stage == STAGE_LOOK_FOR_TAPE) {
                lookForTape();//The robot moves to the side until it sees tape
            } else if (stage == STAGE_ALIGN_WITH_TAPE) {
                alignWithTape();//The robot turns its front or back wheels until both light sensors see tape
            } else if (stage == STAGE_ALIGN_WITH_TAPE_PERFECT) {
//                alignWithTapePerfect();
                endStage();
            } else if (stage == STAGE_BACK_UP) {
                backUp(15 * INCHES_PER_CENT);//TODO this value
//                endStage();
            } else if (stage == STAGE_ALIGN_WITH_WALL_2) {
//                if (!fail) {
//                alignWithWall(29);
                approachDistance(32);

//                } else {
//                    endStage();
//                }
            } else if (stage == STAGE_PUT_DOWN_FLAPS) {
                putDownFlaps();
            } else if (stage == STAGE_READ_COLOR) {
                readColorSensor();//an average of 10 readings or is recorded from the light sensor facing the buttons
            } else if (stage == STAGE_MOVE_BUTTON_FLAP) {
                if (!fail) {
                    moveCorrectButtonFlap();//based on the light detected in the previous stage,
                    // the robot presses the correct button
                } else {
                    endStage();
                }
            } else if (stage == STAGE_APPROACH_BUTTON) {
                approachDistance(25);
            } else if (stage == STAGE_RAM_BUTTON) {
                goForth(2);
//                endStage();
            } else if (stage == STAGE_DROP_CLIMBERS) {
                dropClimbers();//arm controlled by servo moves to drop climbers
            } else if (stage == STAGE_LIFT_ARM) {
                liftArm();//arm is lifted up to let the climbers fall
            } else {
                // moveIntoFloorGoal();
            }
        }
//        logData("rightLight", String.valueOf(rightLightDetected));
//        logData("leftLight", String.valueOf(leftLightDetected));
    }

    protected void endStage() {
        stopRobot();
        resetEncoders();
        stage++;
        startMoveTime = System.currentTimeMillis();
    }

    protected void initServos() {
        if (System.currentTimeMillis() - startMoveTime < 300) {
            climberServo.setPosition(climberServoInitPos);
            rightHookServo.setPosition(rightHookInitPos);
            leftHookServo.setPosition(leftHookInitPos);
            rightFlapServo.setPosition(rightFlapServoInitPos);
            leftFlapServo.setPosition(leftFlapServoInitPos);
            boxSpin.setPosition(spinInitPos);
            boxLift.setPosition(liftInitPos);
            boxTilt.setPosition(tiltInitPos);
            frontLightSensor.enableLed(true);
            backLightSensor.enableLed(true);
        } else {
            endStage();
        }
    }

//    protected void sweepTriangle() {
//        final int rightOut = 0;
//        final int leftTriangle = 1;
//        final int rightTriangle = 2;
//        if (sweepStage == rightOut) {
//            if (System.currentTimeMillis() - startMoveTime < 300) {
//                rightSweepServo.setPosition(rightSweepOut);
//            } else {
//                sweepStage++;
//                startMoveTime = System.currentTimeMillis();
//            }
//        } else if (sweepStage == leftTriangle) {
//            if (System.currentTimeMillis() - startMoveTime < 300) {
//                leftSweepServo.setPosition(leftSweepTriangle);
//            } else {
//                sweepStage++;
//                startMoveTime = System.currentTimeMillis();
//            }
//        } else if (sweepStage == rightTriangle) {
//            if (System.currentTimeMillis() - startMoveTime < 300) {
//                rightSweepServo.setPosition(rightSweepTriangle);
//            } else {
//                sweepStage++;
//                startMoveTime = System.currentTimeMillis();
//            }
//        } else {
//            sweepStage = 0;
//            endStage();
//        }
//    }

    protected void goToOtherWall() {
//        if (System.currentTimeMillis() - startProgramTime > 15000) {
//            backUp(5);
//            fail = true;
//        } else
        if (frontLeftUltra.getUltrasonicLevel() > 25 && frontRightUltra.getUltrasonicLevel() > 25) {//TODO this value
            seesWallLeft = 0;
            seesWallRight = 0;
            if (stoppedForObstacle) {
                distanceToGoIndex++;
                stoppedForObstacle = false;
            }
            distanceToGo[distanceToGoIndex] = goDirectionOfOtherWall(DEFAULT_POWER * 1.5, distanceToGo[distanceToGoIndex - 1], startMoveTime);
        } else if (seesWallLeft < 18 && seesWallRight < 18) {
            seesWallLeft++;
            seesWallRight++;
            if (stoppedForObstacle) {
                distanceToGoIndex++;
                stoppedForObstacle = false;
            }
            distanceToGo[distanceToGoIndex] = goDirectionOfOtherWall(DEFAULT_POWER * 1.5, distanceToGo[distanceToGoIndex - 1], startMoveTime);
        } else if (distanceToGo[distanceToGoIndex] < 30) {
            seesWallLeft = 0;
            seesWallRight = 0;
            endStage();
        } else {
            stopRobot();
            if (!stoppedForObstacle) {
                stoppedForObstacle = true;
                startStoppedTime = System.currentTimeMillis();
            }
            if (System.currentTimeMillis() - startStoppedTime > 0000) {//Temporary (probably) change bck to 5000
//                dropClimbers = false;//There is something wrong, so better save dropping climbers for TeleOp to be safe
                seesWallLeft = 0;
                seesWallRight = 0;
                endStage();
            }
            startMoveTime = System.currentTimeMillis();
        }
    }

    protected abstract double goDirectionOfOtherWall(double power, double inches, long startTime);

    protected void alignWithWall(int distance) {
//        if (sensorInputs < 50) {
//            stopRobot();
//            if (frontLeftUltra.getUltrasonicLevel() < LEFT_ULTRA_PERFECT_DIST) {
//                seesWallLeft++;
//            } else if (seesWallLeft < 15) {
//                seesWallLeft = 0;
//            }
//            if (frontRightUltra.getUltrasonicLevel() < RIGHT_ULTRA_PERFECT_DIST) {
//                seesWallRight++;
//            } else if (seesWallRight < 15) {
//                seesWallRight = 0;
//            }
//            if (frontLeftUltra.getUltrasonicLevel() > LEFT_ULTRA_PERFECT_DIST) {
//                seesOpeningLeft++;
//            } else if (seesOpeningLeft < 40) {
//                seesOpeningLeft = 0;
//            }
//            if (frontRightUltra.getUltrasonicLevel() > RIGHT_ULTRA_PERFECT_DIST) {
//                seesOpeningRight++;
//            } else if (seesOpeningRight < 40) {
//                seesOpeningRight = 0;
//            }
//            sensorInputs++;
//            startMoveTime = System.currentTimeMillis();
//        } else {
//            if (seesOpeningLeft >= 40 && seesOpeningRight >= 40) {
//                double distanceToGo = goDistanceForward(DEFAULT_POWER, 2 * INCHES_PER_CENT, startMoveTime);
//                if (distanceToGo == 0) {
//                    sensorInputs = 0;
//                }
//            } else if (seesOpeningRight >= 15) {
//                double distanceToGo = goDistanceRightWheelsForward(DEFAULT_POWER, 2 * INCHES_PER_CENT, startMoveTime);
//                if (distanceToGo == 0) {
//                    sensorInputs = 0;
//                }
//            } else if (seesOpeningLeft >= 15) {
//                double distanceToGo = goDistanceLeftWheelsForward(DEFAULT_POWER, 2 * INCHES_PER_CENT, startMoveTime);
//                if (distanceToGo == 0) {
//                    sensorInputs = 0;
//                }
//            } else if (seesWallLeft >= 15 && seesWallRight >= 15) {
//                double distanceToGo = goDistanceBackward(DEFAULT_POWER, 2 * INCHES_PER_CENT, startMoveTime);
//                if (distanceToGo == 0) {
//                    sensorInputs = 0;
//                }
//            } else if (seesWallRight >= 15) {
//                double distanceToGo = goDistanceRightWheelsBackward(DEFAULT_POWER, 2 * INCHES_PER_CENT, startMoveTime);
//                if (distanceToGo == 0) {
//                    sensorInputs = 0;
//                }
//            } else if (seesWallLeft >= 15) {
//                double distanceToGo = goDistanceLeftWheelsBackward(DEFAULT_POWER, 2 * INCHES_PER_CENT, startMoveTime);
//                if (distanceToGo == 0) {
//                    sensorInputs = 0;
//                }
//            } else {
//                seesWallRight = 0;
//                seesWallLeft = 0;
//                sensorInputs = 0;
//                endStage();
//            }
//        }
        if (sensorInputs < 35) {
            stopRobot();
            if (frontLeftUltra.getUltrasonicLevel() <= distance) {
                seesWallLeft++;
            } else if (seesWallLeft < 11) {
                seesWallLeft = 0;
            }
            if (frontRightUltra.getUltrasonicLevel() <= distance) {
                seesWallRight++;
            } else if (seesWallRight < 11) {
                seesWallRight = 0;
            }
            sensorInputs++;
            startMoveTime = System.currentTimeMillis();
        } else {
            if (seesWallLeft < 15 && seesWallRight < 15) {
                double distanceToGo = goDistanceForward(DEFAULT_POWER, 4 * INCHES_PER_CENT, startTime);
                if (distanceToGo == 0) {
                    resetEncoders();
                    sensorInputs = 0;
                    alignPeriods++;
                }
            } else if (seesWallRight < 15) {
                double distanceToGo = goDistanceRightWheelsForward(DEFAULT_POWER, 6 * INCHES_PER_CENT, startMoveTime);
                if (distanceToGo == 0) {
                    resetEncoders();
                    sensorInputs = 0;
                    alignPeriods++;
                }
            } else if (seesWallLeft < 15) {
                double distanceToGo = goDistanceLeftWheelsForward(DEFAULT_POWER, 6 * INCHES_PER_CENT, startMoveTime);
                if (distanceToGo == 0) {
                    resetEncoders();
                    sensorInputs = 0;
                    alignPeriods++;
                }
            } else {
                seesWallRight = 0;
                seesWallLeft = 0;
                sensorInputs = 0;
                alignPeriods = 0;
                endStage();
            }
        }
        if (alignPeriods > 15) {
            seesWallRight = 0;
            seesWallLeft = 0;
            sensorInputs = 0;
            alignPeriods = 0;
            endStage();
        }
    }

//    protected void openSweepers() {
//        if (System.currentTimeMillis() - startMoveTime < 300) {
//            rightSweepServo.setPosition(rightSweepOut);
//            leftSweepServo.setPosition(leftSweepOut);
//        } else {
//            endStage();
//        }
//    }

//    protected void closeSweepers() {
//        final int leftIn = 0;
//        final int rightIn = 1;
//        if (sweepStage == leftIn) {
//            if (System.currentTimeMillis() - startMoveTime < 300) {
//                leftSweepServo.setPosition(leftSweepIn);
//            } else {
//                sweepStage++;
//                startMoveTime = System.currentTimeMillis();
//            }
//        } else if (sweepStage == rightIn) {
//            if (System.currentTimeMillis() - startMoveTime < 300) {
//                rightSweepServo.setPosition(rightSweepIn);
//            } else {
//                sweepStage++;
//                startMoveTime = System.currentTimeMillis();
//            }
//        } else {
//            sweepStage = 0;
//            endStage();
//        }
//    }

    protected void putDownFlaps() {
        if (System.currentTimeMillis() - startMoveTime < 300) {
            rightFlapServo.setPosition(rightFlapServoFinalPos);
            leftFlapServo.setPosition(leftFlapServoFinalPos);
        } else {
            endStage();
        }
    }

    protected void ramWall() {
        double distanceToGo = goDistanceForward(DEFAULT_POWER * 0.625, 16, startMoveTime);
        if (distanceToGo == 0) {
            endStage();
        }
    }

    protected void backUp(double distance) {
        double distanceToGo = goDistanceBackward(DEFAULT_POWER / 2.0, distance, startMoveTime);
        if (distanceToGo == 0) {
            endStage();
        }
    }

    protected abstract void lookForTape();

    protected abstract void alignWithTape();

    protected abstract void alignWithTapePerfect();

    protected abstract void moveCorrectButtonFlap();

    protected void approachDistance(int distance) {
        if (frontLeftUltra.getUltrasonicLevel() > distance && frontRightUltra.getUltrasonicLevel() > distance) {
            seesWallLeft = 0;
            seesWallRight = 0;
            goForward(DEFAULT_POWER / 2.0);
        } else if (seesWallLeft < 11 && seesWallRight < 11) {
            seesWallLeft++;
            seesWallRight++;
            goForward(DEFAULT_POWER / 2.0);
        } else {
            stopRobot();
            seesWallLeft = 0;
            seesWallRight = 0;
            endStage();
        }
    }

    protected void goForth(double distance) {
        double distanceToGo = goDistanceForward(DEFAULT_POWER / 2.0, distance, startMoveTime);
        if (distanceToGo == 0) {
            endStage();
        }
    }

    protected abstract void moveIntoFloorGoal();

    protected void dropClimbers() {
        if (dropClimbers) {
            if (System.currentTimeMillis() - startMoveTime < 500) {
                climberServo.setPosition(climberServoFinalPos);
            } else {
                endStage();
            }
        } else {
            endStage();
        }
    }

    protected void liftArm() {
        if (System.currentTimeMillis() - startMoveTime < 500) {
            climberServo.setPosition(climberServoInitPos);
        } else {
            endStage();
        }
    }

    protected void readColorSensor() {
        if (colorSensorInputs < 100) {
            rightLightDetected += rightColorSensor.getLightDetected();
            leftLightDetected += leftColorSensor.getLightDetected();
            colorSensorInputs++;
        } else {
            rightLightDetected /= 100.0;
            leftLightDetected /= 100.0;
            endStage();
        }
    }

    protected void moveRightFlap() {
        if (System.currentTimeMillis() - startMoveTime < 1000) {
            rightFlapServo.setPosition(rightFlapServoFinalPos);
            leftFlapServo.setPosition(leftFlapServoInitPos);
        } else {
            endStage();
        }
    }

    protected void moveLeftFlap() {
        if (System.currentTimeMillis() - startMoveTime < 1000) {
            leftFlapServo.setPosition(leftFlapServoFinalPos);
            rightFlapServo.setPosition(rightFlapServoInitPos);
        } else {
            endStage();
        }
    }
}
