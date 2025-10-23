package chapter3.example1;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    static final String SOURCE_FILE = "src/chapter3/example1/resources/many-flowers.jpg";
    static final String DESTINATION_FILE = "src/chapter3/example1/out/many-flowers.jpg";

    static void main() throws IOException {
        BufferedImage image = ImageIO.read(new File(SOURCE_FILE));
        BufferedImage resultImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        long startTime = System.currentTimeMillis();
        recolorSingleThreaded(image, resultImage);
        long endTime = System.currentTimeMillis();
        System.out.println("Single thread time: " + (endTime - startTime) + " ms");

        long startTime2 = System.currentTimeMillis();
        recolorMultiThreaded(image, resultImage, 5);
        long endTime2 = System.currentTimeMillis();
        System.out.println("Multi thread time: " + (endTime2 - startTime2) + " ms");

        File outputFile = new File(DESTINATION_FILE);
        File parent = outputFile.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs(); // 상위 디렉터리 생성
        }
        ImageIO.write(resultImage, "jpg", outputFile);
    }

    // 리컬러링을 멀티 스레드로 처리
    private static void recolorMultiThreaded(BufferedImage image, BufferedImage resultImage, int numberOfThreads) {
        List<Thread> threads = new ArrayList<>();
        int width = image.getWidth();
        int height = image.getHeight() / numberOfThreads;

        for (int i = 0; i < numberOfThreads; i++) {
            final int threadMultiplier = i;
            Thread thread = new Thread(() -> {
               int leftCorner = 0;
               int topCorner = threadMultiplier * height;

               recolorImage(image, resultImage, leftCorner, topCorner, width, height);
            });

            threads.add(thread);
        }

        for (Thread thread: threads) {
            thread.start();
        }

        for (Thread thread: threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
            }
        }
    }

    // 단일 스레드로 이미지 리컬러링
    private static void recolorSingleThreaded(BufferedImage image, BufferedImage resultImage) {
        recolorImage(image, resultImage, 0, 0, image.getWidth(), image.getHeight());
    }

    static void recolorImage(BufferedImage originalImage, BufferedImage resultImage, int leftX, int topY, int width, int height) {
        for (int x = leftX; x < leftX + width && x < originalImage.getWidth(); x++) {
            for (int y = topY; y < topY + height && y < originalImage.getHeight(); y++) {
                recolorPixel(originalImage, resultImage, x, y);
            }
        }
    }

    static void recolorPixel(BufferedImage originalImage, BufferedImage resultImage, int x, int y) {
        int rgb = originalImage.getRGB(x, y);

        int red = getRed(rgb);
        int green = getGreen(rgb);
        int blue = getBlue(rgb);

        int newRed, newGreen, newBlue;

        if (isShadeOfGray(red, green, blue)) {
            newRed = Math.min(255, red + 10);
            newGreen = Math.max(0, green - 80);
            newBlue = Math.max(0, blue - 20);
        } else {
            newRed = red;
            newGreen = green;
            newBlue = blue;
        }

        int newRGB = createRGBFromColors(newRed, newGreen, newBlue);
        setRGB(resultImage, x, y, newRGB);
    }

    static void setRGB(BufferedImage image, int x, int y, int rgb) {
        image.getRaster().setDataElements(x, y, image.getColorModel().getDataElements(rgb, null));
    }

    static boolean isShadeOfGray(int red, int green, int blue) {
        return Math.abs(red - green) < 30 && Math.abs(red - blue) < 30 && Math.abs(green - blue) < 30;
    }

    static int createRGBFromColors(int red, int green, int blue) {
        int rgb = 0;
        rgb |= blue;
        rgb |= green << 8;
        rgb |= red << 16;

        rgb |= 0xFF000000;
        return rgb;
    }

    static int getGreen(int rgb) {
        return (rgb & 0x0000FF00) >> 8;
    }

    static int getBlue(int rgb) {
        return rgb & 0x000000FF;
    }

    static int getRed(int rgb) {
        return (rgb & 0x00FF0000) >> 16;
    }
}
