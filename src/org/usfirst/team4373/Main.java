package org.usfirst.team4373;

import com.github.sarxos.webcam.ds.ipcam.IpCamDevice;
import com.github.sarxos.webcam.ds.ipcam.IpCamDeviceRegistry;
import com.github.sarxos.webcam.ds.ipcam.IpCamMode;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;


public class Main {

    public static void main(String[] args) throws Exception {
        IpCamDeviceRegistry.register("Lignano", "http://10.43.73.74/mjpg/video.mjpg", IpCamMode.PUSH);
        IpCamDevice capture = IpCamDeviceRegistry.getIpCameras().get(0);
        capture.open();
        capture.setFailOnError(true);
        while (!capture.isOnline()) ;
        System.out.println("2");
        BufferedImage image = capture.getImage();
        File outputFile = new File("/Users/derros/Documents/test.jpg");
        System.out.println(": " + image.getHeight() + "x" + image.getWidth());
        ImageIO.write(image, "jpg", outputFile);

    }
}