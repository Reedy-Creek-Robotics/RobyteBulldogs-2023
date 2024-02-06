package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
@TeleOp
public class cameraTest extends LinearOpMode {

    VisionPortal visionPortal;
    BlueRecognitionProcessor recognitionProcessorSample;

    @Override
    public void runOpMode() throws InterruptedException {
        CameraName camera = hardwareMap.get(WebcamName.class, "Webcam 1");
        recognitionProcessorSample = new BlueRecognitionProcessor();
        visionPortal = new VisionPortal.Builder()
                .setCamera(camera)
                .addProcessor(recognitionProcessorSample)
                .build();

        waitForStart();

    }
}
