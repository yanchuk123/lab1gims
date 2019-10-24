package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void noiz(int porog) throws IOException {

        BufferedImage im = ImageIO.read(new File("file.jpg"));
        int rx = 0;
        int ry = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        for (int i = 0; i < im.getHeight() * im.getWidth() * porog / 100; i++) {
            rx = 0 + (int) (Math.random() * im.getHeight());
            ry = 0 + (int) (Math.random() * im.getWidth());
            newR = 1 + (int) (Math.random() * 255);
            newG = 1 + (int) (Math.random() * 255);
            newB = 1 + (int) (Math.random() * 255);
            Color newCol = new Color(newR, newG, newB);

            im.setRGB(ry, rx, newCol.getRGB());

        }

        ImageIO.write(im, "jpg", new File("file1.jpg"));


    }


    public static void filtr() throws IOException {
        int[] masR = new int[5];
        int[] masG = new int[5];
        int[] masB = new int[5];
        BufferedImage im = ImageIO.read(new File("file1.jpg"));


        for (int i = 1; i < im.getHeight() - 1; i++) {
            for (int j = 1; j < im.getWidth() - 1; j++) {

                Color col = new Color(im.getRGB(j, i));
                Color col1 = new Color(im.getRGB(j, i + 1));
                Color col2 = new Color(im.getRGB(j + 1, i));
                Color col3 = new Color(im.getRGB(j, i - 1));
                Color col4 = new Color(im.getRGB(j - 1, i));
                masR[0] = col.getRed();
                masR[1] = col1.getRed();
                masR[2] = col2.getRed();
                masR[3] = col3.getRed();
                masR[4] = col4.getRed();

                masG[0] = col.getGreen();
                masG[1] = col1.getGreen();
                masG[2] = col2.getGreen();
                masG[3] = col3.getGreen();
                masG[4] = col4.getGreen();


                masB[0] = col.getBlue();
                masB[1] = col1.getBlue();
                masB[2] = col2.getBlue();
                masB[3] = col3.getBlue();
                masB[4] = col4.getBlue();
                Arrays.sort(masR);
                Arrays.sort(masG);
                Arrays.sort(masB);
                Color newCOl = new Color(masR[2], masG[2], masB[2]);
                im.setRGB(j, i, newCOl.getRGB());
            }

        }
        ImageIO.write(im, "jpg", new File("fileFilt.jpg"));

    }

    public static void cont(double porog) throws IOException {
        BufferedImage im = ImageIO.read(new File("file.jpg"));

        double brightmain = 0;
        double brighttop = 0;
        double brightbot = 0;
        double brightleft = 0;
        double brightright = 0;
        double f = 0;
        double min = 0;
        double max = 0;
        double[][] masf = new double[im.getHeight()][im.getWidth()];
        Color newWhite = new Color(255, 255, 255);
        Color newBlack = new Color(0, 0, 0);

        for (int i = 1; i < im.getHeight() - 1; i++) {
            for (int j = 1; j < im.getWidth() - 1; j++) {

                Color colmain = new Color(im.getRGB(j, i));
                Color colbot = new Color(im.getRGB(j, i + 1));
                Color colright = new Color(im.getRGB(j + 1, i));
                Color coltop = new Color(im.getRGB(j, i - 1));
                Color colleft = new Color(im.getRGB(j - 1, i));


                brightmain = (0.3 * colmain.getRed() + 0.6 * colmain.getGreen() + 0.1 * colmain.getBlue());
                brighttop = (0.3 * coltop.getRed() + 0.6 * coltop.getGreen() + 0.1 * coltop.getBlue());
                brightbot = (0.3 * colbot.getRed() + 0.6 * colbot.getGreen() + 0.1 * colbot.getBlue());
                brightleft = (0.3 * colleft.getRed() + 0.6 * colleft.getGreen() + 0.1 * colleft.getBlue());
                brightright = (0.3 * colright.getRed() + 0.6 * colright.getGreen() + 0.1 * colright.getBlue());
                f = Math.log(brightmain / brightbot) + Math.log(brightmain / brighttop) + Math.log(brightmain / brightleft) + Math.log(brightmain / brightright);
                // f = Math.log((Math.pow(brightmain,4))/(brighttop*brightbot*brightleft*brightright));
                masf[i][j] = f;


                //           System.out.println(+f);

            }
        }
        for (int i = 0; i < im.getHeight(); i++) {
            for (int j = 0; j < im.getWidth(); j++) {
                if (min > masf[i][j]) {
                    min = masf[i][j];
                }
                if (max < masf[i][j]) {
                    max = masf[i][j];
                }
            }

        }
        for (int i = 0; i < im.getHeight(); i++) {
            for (int j = 0; j < im.getWidth(); j++) {
                masf[i][j] += Math.abs(min);
                masf[i][j] = masf[i][j] * 255 / (max + Math.abs(min));
            }
        }

        for (int i = 0; i < im.getHeight(); i++) {
            for (int j = 0; j < im.getWidth(); j++) {
                if (masf[i][j] > porog) {
                    im.setRGB(j, i, newBlack.getRGB());
                }
                if (masf[i][j] <= porog) {
                    im.setRGB(j, i, newWhite.getRGB());

                }
            }

        }
        System.out.println(min + "  " + max);
        ImageIO.write(im, "jpg", new File("fileCor.jpg"));
    }

    public static void texture() throws IOException {

        BufferedImage im = ImageIO.read(new File("file.jpg"));
        BufferedImage tex = ImageIO.read(new File("kirpich.bmp"));
        Scanner sc = new Scanner(System.in);
        System.out.println("Текстурирование изображения другим изображением в рамке NxN, введите N");
        int N = sc.nextInt();
        while ((N >= im.getWidth() || N >= im.getHeight() || N <= 0)) {
            System.out.println("Неверное N, повторите ввод");
            N = sc.nextInt();
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                Color coltex = new Color(tex.getRGB(j % tex.getWidth(), i % tex.getHeight()));
                im.setRGB(j, i, coltex.getRGB());
            }

        }
        ImageIO.write(im, "bmp", new File("textur.bmp"));


    }


    public static void main(String[] args) throws IOException {
        //noiz(15);
        //filtr();
        //cont(175);
        texture();
    }
}
