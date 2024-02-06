package org.firstinspires.ftc.teamcode;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp
public class FieldRelativeOpMode extends LinearOpMode {
    @Override
     public void runOpMode() throws InterruptedException {
        // Declare our motors
        DcMotor frontLeft = hardwareMap.dcMotor.get("frontLeft");
        DcMotor backLeft = hardwareMap.dcMotor.get("backLeft");
        DcMotor frontRight = hardwareMap.dcMotor.get("frontRight");
        DcMotor backRight = hardwareMap.dcMotor.get("backRight");
        DcMotor rightSlide = hardwareMap.dcMotor.get("rightSlide");
        DcMotor leftSlide = hardwareMap.dcMotor.get("leftSlide");

        // 0 frontRight
        // 1 frontLeft
        // 2 backLeft
        // 3 backRight
        // Expansion Hub
        // 0 frontLeft
        // 1 frontRight


        // Reverse the right side motors.
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        Servo armServo = hardwareMap.servo.get("armServo");
        Servo fingerServo = hardwareMap.servo.get("fingerServo");
        Servo clawServo = hardwareMap.servo.get("clawServo");

        double speedselect = 0.8;

        // Retrieve the IMU from the hardware map
        IMU imu = hardwareMap.get(IMU.class, "imu");
        // Adjust the orientation parameters to match your robot
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.BACKWARD,
                RevHubOrientationOnRobot.UsbFacingDirection.UP));
        // Without this, the REV Hub's orientation is assumed to be logo up / USB forward
        imu.initialize(parameters);

        imu.resetYaw();

        waitForStart();

        if (isStopRequested()) return;
        fingerServo.setPosition(0.35);
        clawServo.setPosition(0);
        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x;



            String teamNumber = "Team 24469";
            if (gamepad1.x){
                imu.resetYaw();
                imu.initialize(parameters);
                sleep(100);
                imu.resetYaw();
                imu.initialize(parameters);
            }

            if (gamepad1.left_trigger > 0.5f){
                armServo.setPosition(0);
            }
            if (gamepad1.right_trigger > 0.5f){
                armServo.setPosition(0.5f);
            }

            if (gamepad1.left_bumper){
                fingerServo.setPosition(0.15);
                sleep(200);
            }
            if (gamepad1.right_bumper){
                fingerServo.setPosition(0);
                sleep(200);
            }


            if (gamepad1.b) {
                int armDownPosition = 0;
                leftSlide.setTargetPosition(armDownPosition);
                rightSlide.setTargetPosition(armDownPosition);
                leftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rightSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                leftSlide.setPower(1.0);
                rightSlide.setPower(1.0);
            } else if (gamepad1.a) {
                int armUpPositionleft = -4300;
                int armUpPositionright = 4300;
                leftSlide.setTargetPosition(armUpPositionleft);
                rightSlide.setTargetPosition(armUpPositionright);
                leftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rightSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                leftSlide.setPower(1.0);
                rightSlide.setPower(1.0);
            } else {
                leftSlide.setTargetPosition(leftSlide.getCurrentPosition());
                rightSlide.setTargetPosition(rightSlide.getCurrentPosition());
                leftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rightSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                leftSlide.setPower(0.8);
                rightSlide.setPower(0.8);
            }

            double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

            // Rotate the movement direction counter to the bot's rotation
            double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
            double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

            rotX = rotX * 1.1;  // Counteract imperfect strafing

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio,
            // but only if at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
            double frontLeftPower = (rotY + rotX + rx) / denominator;
            double backLeftPower = (rotY - rotX + rx) / denominator;
            double frontRightPower = (rotY - rotX - rx) / denominator;
            double backRightPower = (rotY + rotX - rx) / denominator;

            if (gamepad1.dpad_up) {
                speedselect = speedselect + 0.3;
                sleep(200);
            } else if (gamepad1.dpad_down) {
                speedselect = speedselect - 0.3;
                sleep(200);
            }
            telemetry.addData("Current speed: ", speedselect);
            telemetry.update();

            frontLeft.setPower(frontLeftPower * speedselect);
            backLeft.setPower(backLeftPower * speedselect);
            frontRight.setPower(frontRightPower * speedselect);
            backRight.setPower(backRightPower * speedselect);

        }
    }
}
