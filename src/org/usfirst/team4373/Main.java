package org.usfirst.team4373;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

//import javax.swing.*;
//import java.awt.*;


public class Main {


    private static final String USER_AGENT = "Mozilla/5.0";

    private static final String CONTENT_LENGTH = "Content-Length: ";
    private static final String CONTENT_TYPE = "Content-Type: image/jpeg";
    private static int contentLength(String header) {
        int indexOfContentLength = header.indexOf(CONTENT_LENGTH);
        int valueStartPos = indexOfContentLength + CONTENT_LENGTH.length();
        int indexOfEOL = header.indexOf('\n', indexOfContentLength);

        String lengthValStr = header.substring(valueStartPos, indexOfEOL).trim();

        int retValue = Integer.parseInt(lengthValStr);

        return retValue;

    }


    public static void main(String[] args) throws Exception {

        System.out.println("Init");
        String url = args[0];

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        byte[] ib = null;
        ByteArrayInputStream bi = null;
        BufferedImage i;
        int counter = 0;
        System.out.println("hi");
        while (true) {
            System.out.println("in loop");
            ib = nextIb(con.getInputStream());
            bi = new ByteArrayInputStream(ib);
            i = ImageIO.read(bi);
            ++counter;
            System.out.println("Got frame [" + counter + "] : Height " + i.getHeight() + "; Width: " + i.getWidth());
        }

        /*
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);
        System.out.println("Content length: " + con.getContentType() + " " + con.getContentLengthLong());
        BufferedImage img = ImageIO.read(con.getInputStream());
        System.out.println("img.flush");
        img.flush();
        System.out.println(" - - ");
        System.out.println("Image height is: " + img.getHeight());

        ImageIcon icon = new ImageIcon(img);
        JFrame frame = new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(200, 300);
        JLabel lbl = new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        */

    }

    /**
     * Using the urlStream get the next JPEG image as a byte[]
     *
     * @return byte[] of the JPEG
     * @throws IOException
     */
    private static byte[] nextIb(InputStream urlStream) throws IOException {
        System.out.println("test ib");
        boolean isHeaderFrame = false;
        int currByte = -1;
        StringWriter stringWriter = new StringWriter();
        String header = null;
        // go to headers
        while ((currByte = urlStream.read()) > -1) {
            stringWriter.write(currByte);
            System.out.println("str1 - " + stringWriter.toString());
            if (stringWriter.toString().indexOf("myboundary") > 0) break;
        }

        System.out.println("str1 - skipped myboundary");
        // skip Content-Type
        while ((currByte = urlStream.read()) != '\n') {System.out.print(currByte);}
        System.out.println("\nContent-Type skipped...");
        // fetch Content-Length
        stringWriter.close();
        stringWriter = new StringWriter();
        while ((currByte = urlStream.read()) > 0) {
            stringWriter.write(currByte);
            System.out.println(stringWriter.toString());
            if (stringWriter.toString().equals("Content-Length: ")) break;
        }
        // now we're content length
        stringWriter.close();
        stringWriter = new StringWriter();
        while ((currByte = urlStream.read()) != '\n') {
            stringWriter.write(currByte);
        }
        int length = Integer.parseInt(stringWriter.toString());
        System.out.println(length);
        // read in
        byte[] imageBytes = new byte[length];
        urlStream.read(imageBytes);
        return imageBytes;
    }
}
