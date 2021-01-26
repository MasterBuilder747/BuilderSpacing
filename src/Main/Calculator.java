package Main;

import java.util.Scanner;

public class Calculator {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        //minutes not supported and not necessary
        System.out.print("Enter second build time (ex: 6d18h): ");
        String build2 = sc.next();
        System.out.print("Enter first build time (ex: 1d20h): ");
        String build1 = sc.next();
        System.out.print("Enter desired build time: (ex: 3d; ex2: 4d16h; ex3: 3h): ");
        String build = sc.next();

        //conversions
        //decimal is in days
        String bld = build.replace(" ", "");
        String bld1 = build1.replace(" ", "");
        String bld2 = build2.replace(" ", "");
        double b, b1, b2;
        if (bld.contains("d")) {
            b = Double.parseDouble(bld.substring(0, bld.indexOf("d")));
            if (bld.contains("h")) {
                double hours = Double.parseDouble(bld.substring(bld.indexOf("d") + 1, bld.indexOf("h")));
                //add hours to day value
                if (hours < 24) {
                    b += hours / 24;
                } else {
                    throw new IllegalArgumentException("Hours must be less than 24 for build times.");
                }
            }
        } else if (bld.contains("h")) {
            //hours only
            b = Double.parseDouble(bld.substring(0, bld.indexOf("h"))) / 24;
        } else {
            //invalid
            throw new IllegalArgumentException("Invalid build input.");
        }
        if (bld1.contains("d")) {
            b1 = Double.parseDouble(bld1.substring(0, bld1.indexOf("d")));
            if (bld1.contains("h")) {
                double hours = Double.parseDouble(bld1.substring(bld1.indexOf("d") + 1, bld1.indexOf("h")));
                //add hours to day value
                if (hours < 24) {
                    b1 += hours / 24;
                } else {
                    throw new IllegalArgumentException("Hours must be less than 24 for build times.");
                }
            }
        } else if (bld1.contains("h")) {
            //hours only
            b1 = Double.parseDouble(bld1.substring(0, bld1.indexOf("h"))) / 24;
        } else {
            //invalid
            throw new IllegalArgumentException("Invalid build1 input.");
        }
        if (bld2.contains("d")) {
            b2 = Double.parseDouble(bld2.substring(0, bld2.indexOf("d")));
            if (bld2.contains("h")) {
                double hours = Double.parseDouble(bld2.substring(bld2.indexOf("d") + 1, bld2.indexOf("h")));
                //add hours to day value
                if (hours < 24) {
                    b2 += hours / 24;
                } else {
                    throw new IllegalArgumentException("Hours must be less than 24 for build times.");
                }
            }
        } else if (bld2.contains("h")) {
            //hours only
            b2 = Double.parseDouble(bld2.substring(0, bld2.indexOf("h"))) / 24;
        } else {
            //invalid
            throw new IllegalArgumentException("Invalid build2 input.");
        }

        //formula: timeNeededToWaitToStartDesiredBuild =
        //build1 - [build{converted} - {builder spacing: }[(build2{converted} - build1{converted}) / 2]]
        if (b > b2) {
            throw new IllegalArgumentException("The build time of the desired build is longer than the second build.");
        }
        double t = b1 - (b - ((b2 - b1) / 2));

        //convert back and output
        String days = "";
        String hours = "";
        int h = 0, d = 0;
        if (t > 1) {
            //days and hours
            d = (int)t;
            h = (int)Math.round(24 * (t - d));
            System.out.println("Time needed to wait until you can start this build: " + d + " days, " + h + " hours");
        } else if (t < 1 && t > 0) {
            //hours only
            h = (int)Math.round(24 * t);
            System.out.println("Time needed to wait until you can start this build: " + h + " hours");
        }
    }
}