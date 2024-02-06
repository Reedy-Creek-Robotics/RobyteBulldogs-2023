package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.BlueRecognitionProcessor;
import org.firstinspires.ftc.vision.VisionPortal;

@Autonomous(name="blueRightAuto", group="Robot")
public class blueRightAuto_MASTER extends LinearOpMode {

    private DcMotor frontLeft   = null;
    private DcMotor frontRight  = null;
    private DcMotor backLeft   = null;
    private DcMotor backRight  = null;
    private Servo fingerServo = null;
    private Servo clawServo = null;
    private ElapsedTime     runtime = new ElapsedTime();

    static final double     COUNTS_PER_MOTOR_REV    = 537.7 ;
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.14159);
    static final double     DRIVE_SPEED             = 0.6;
    static final double     TURN_SPEED              = 0.5;

    VisionPortal visionPortal;
    BlueRecognitionProcessor recognitionProcessorSample;

    @Override
    public void runOpMode() {

        frontLeft  = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft  = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        Servo fingerServo = hardwareMap.servo.get("fingerServo");
        Servo clawServo = hardwareMap.servo.get("clawServo");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        CameraName camera = hardwareMap.get(WebcamName.class, "Webcam 1");
        recognitionProcessorSample = new BlueRecognitionProcessor();
        visionPortal = new VisionPortal.Builder()
                .setCamera(camera)
                .addProcessor(recognitionProcessorSample)
                .build();

        telemetry.addData("Starting at",  "%7d :%7d :%7d :%7d",
                frontLeft.getCurrentPosition(),
                frontRight.getCurrentPosition(),
                backLeft.getCurrentPosition(),
                backRight.getCurrentPosition());
        telemetry.update();

        waitForStart();
// Values are frontLeft, frontRight, backLeft, backRight. All are in inches, except turning which is in degrees.
        // Red
        /*
        forward(24);
        backward(12);
        turnRight(90);
        forward(48);*/

        // Green
        /*
        forward(24);
        backward(12);
        strafeLeft(22);
        forward(30);
        turnRight(90);
        forward(110);*/

        
        // Orange
        telemetry.addData("Path:", recognitionProcessorSample.runAuto);
        telemetry.update();
        if (recognitionProcessorSample.runAuto == "center") {
            fingerServo.setPosition(0);
            forward(27);
            clawServo.setPosition(0);
            fingerServo.setPosition(0.15);
            backward(18);
            clawServo.setPosition(0);
            strafeRight(23);
            clawServo.setPosition(0);
            forward(37);
            clawServo.setPosition(0);
            turnLeft(93);
            clawServo.setPosition(0);
            forward(102);
        } else if (recognitionProcessorSample.runAuto == "right") {
            fingerServo.setPosition(0);
            strafeRight(12);
            clawServo.setPosition(0);
            fingerServo.setPosition(0.15);
            forward(20);
            clawServo.setPosition(0);
            backward(10);
            clawServo.setPosition(0);
            strafeRight(13);
            clawServo.setPosition(0);
            forward(39);
            clawServo.setPosition(0);
            turnLeft(93);
            clawServo.setPosition(0);
            forward(101);
            clawServo.setPosition(0);
        } else if (recognitionProcessorSample.runAuto == "left") {
            fingerServo.setPosition(0);
            forward(24);
            turnLeft(93);
            fingerServo.setPosition(0.15);;
            forward(4);
            backward(13);
            strafeRight(27);
            forward(110);
        }
        // Brown
     /* forward(24);
        backward(24);
        turnLeft(90);
        forward(48);*/




        sleep(1000);
    }

    public void encoderDrive(double speed,
                             double frontLeftInches, double frontRightInches,
                             double backLeftInches, double backRightInches,
                             double timeoutS) {
        int newFrontLeftTarget;
        int newFrontRightTarget;
        int newBackLeftTarget;
        int newBackRightTarget;

        if (opModeIsActive()) {

            newFrontLeftTarget = frontLeft.getCurrentPosition() + (int)(frontLeftInches * COUNTS_PER_INCH);
            newFrontRightTarget = frontRight.getCurrentPosition() + (int)(frontRightInches * COUNTS_PER_INCH);
            newBackLeftTarget = backLeft.getCurrentPosition() + (int)(backLeftInches * COUNTS_PER_INCH);
            newBackRightTarget = backRight.getCurrentPosition() + (int)(backRightInches * COUNTS_PER_INCH);
            frontLeft.setTargetPosition(newFrontLeftTarget);
            frontRight.setTargetPosition(newFrontRightTarget);
            backLeft.setTargetPosition(newBackLeftTarget);
            backRight.setTargetPosition(newBackRightTarget);

            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            runtime.reset();
            frontLeft.setPower(Math.abs(speed));
            frontRight.setPower(Math.abs(speed));
            backLeft.setPower(Math.abs(speed));
            backRight.setPower(Math.abs(speed));

            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (frontLeft.isBusy() && frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy())) {

                telemetry.addData("Running to",  " %7d :%7d :%7d :%7d", newFrontLeftTarget,  newFrontRightTarget, newBackLeftTarget, newBackRightTarget);
                telemetry.addData("Currently at",  " at %7d :%7d :%7d :%7d",
                        frontLeft.getCurrentPosition(), frontRight.getCurrentPosition(), backLeft.getCurrentPosition(), backRight.getCurrentPosition());
                telemetry.update();
            }

            frontLeft.setPower(0);
            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);


            frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(250);
        }
    }
    void forward(float distance){
        encoderDrive(DRIVE_SPEED,  distance,  distance, distance, distance, 5.0);
    }
    void backward(float distance){
        encoderDrive(DRIVE_SPEED,  -distance,  -distance, -distance, -distance, 5.0);
    }
    void strafeLeft(float distance){
        encoderDrive(DRIVE_SPEED,  -distance,  distance, distance, -distance, 5.0);
    }
    void strafeRight(float distance){
        encoderDrive(DRIVE_SPEED,  distance,  -distance, -distance, distance, 5.0);
    }
    void turnLeft(float angle){
        encoderDrive(TURN_SPEED,   -(angle/4.43), angle/4.43, -(angle/4.43), angle/4.43, 4.0);
    }
    void turnRight(float angle){
        encoderDrive(TURN_SPEED,   angle/4.43, -(angle/4.43), angle/4.43, -(angle/4.43), 4.0);
    }
    void fingerPosition(float position){
        fingerServo.setPosition(position);
    }
}