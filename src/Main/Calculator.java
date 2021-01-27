package Main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Calculator {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyyHHmm");
        Date date = new Date(System.currentTimeMillis());
        String currentTime = formatter.format(date);
        //System.out.println(currentTime);
        int month = Integer.parseInt(currentTime.substring(0, 2));
        int day = Integer.parseInt(currentTime.substring(2, 4));
        int year = Integer.parseInt(currentTime.substring(4, 8));
        int hour = Integer.parseInt(currentTime.substring(8, 10));
        //System.out.println(month + "\n" + day + "\n" + year + "\n" + hour + "\n" + minute);

        //minutes not supported and not necessary
        System.out.print("Enter first build time (ex: 20h): ");
        String build1 = sc.next();
        System.out.print("Enter second build time (ex: 6d): ");
        String build2 = sc.next();
        System.out.print("Enter desired build time. Must be less than 2nd build time (ex: 4d16h): ");
        String build = sc.next();
        System.out.println();

        //conversions and checks
        //decimal is in days
        double b = timeToDouble(build);
        double b1 = timeToDouble(build1);
        double b2 = timeToDouble(build2);
        if (b < 0 || b1 < 0 || b2 < 0) {
            //invalid
            throw new IllegalArgumentException("Invalid build input.");
        }
        if (b2 < b1 || b2 == b1) {
            throw new IllegalArgumentException("First build time must be greater than second build time.");
        }
        //limit the max build time to 30 days as Coc build times are never that high
        //and would break the calendar system if so
        if (b > 28 || b1 > 28 || b2 > 28) {
            throw new IllegalArgumentException("Build time is too long (Max: 28).");
        }

        //formula: timeNeededToWaitToStartDesiredBuild =
        //build1 - [build{converted} - {builder spacing: }[(build2{converted} - build1{converted}) / 2]]
        if (b > b2) {
            throw new IllegalArgumentException("The build time of the desired build is longer than the second build.");
        }
        //the spacing between b1 and b,
        //which is equal to the space between b and b2 as well
        double s = (b2 - b1) / 2;
        double t = b1 - (b - s);

        //convert back and output spacing and time to wait
        int[] space = doubleToTime(s);
        int[] wait = doubleToTime(t);
        if (space != null && wait != null) {

            //display the builder spacing of b in between b1 and b2
            if (space.length == 2) {
                System.out.println("Builder spacing: " + space[0] + " days, " + space[1] + " hours");
            } else if (space.length == 1) {
                System.out.println("Builder spacing: " + space[0] + " hours");
            }

            //find the time needed to wait until the build can be started
            if (wait.length == 2) {
                day += wait[0];
                hour += wait[1];
                System.out.println("Time needed to wait until you can start this build: " + wait[0] + " days, " + wait[1] + " hours");
            } else if (wait.length == 1) {
                hour += wait[0];
                System.out.println("Time needed to wait until you can start this build: " + wait[0] + " hours");
            }

            //find the time, day, month, and year to start the build
            //change values accordingly
            //31 days: 1, 3, 5, 7, 8, 10, 12
            //30 days: 4, 6, 9, 11
            //28 or 29 days: 2
            if (hour > 23) {
                hour -= 24;
                day++;
            }
            if (month == 4 || month == 6 || month == 9 || month == 11) {
                //30
                if (day > 30) {
                    day -= 30;
                    month++;
                }
            } else if (month == 2) {
                if (year % 4 == 0) {
                    //29
                    if (day > 29) {
                        day -= 29;
                        month++;
                    }
                } else {
                    //28
                    if (day > 28) {
                        day -= 28;
                        month++;
                    }
                }
            } else {
                //31
                if (day > 31) {
                    day -= 31;
                    month++;
                }
            }
            if (month > 12) {
                month = 1;
                year++;
            }

            //am/pm conversion
            if (hour > 12) {
                //pm
                hour -= 12;
                System.out.println("You can start the build on " + month + "-" + day + "-" + year + " at approximately " + hour + "pm");
            } else {
                //am
                System.out.println("You can start the build on " + month + "-" + day + "-" + year + " at approximately " + hour + "am");
            }
        }
    }

    //converts a string of days/hours to a double
    //format: DDdHHh or DdHh
    public static double timeToDouble(String time2) {
        String time = time2.replace(" ", "");
        double out = -1;
        if (time.contains("d")) {
            out = Double.parseDouble(time.substring(0, time.indexOf("d")));
            if (time.contains("h")) {
                double hours = Double.parseDouble(time.substring(time.indexOf("d") + 1, time.indexOf("h")));
                //add hours to day value
                if (hours < 24) {
                    out += hours / 24;
                } else {
                    throw new IllegalArgumentException("Hours must be less than 24.");
                }
            }
        } else if (time.contains("h")) {
            //hours only
            out = Double.parseDouble(time.substring(0, time.indexOf("h"))) / 24;
        }
        return out;
    }
    public static int[] doubleToTime(double d) {
        int[] out = null;
        if (d > 1) {
            //days, hours
            out = new int[2];
            out[0] = (int)d;
            out[1] = (int)Math.round(24 * (d - out[0]));
        } else if (d == 1) {
            out = new int[2];
            out[0] = (int)d;
        } else if (d < 1 && d > 0) {
            //hours
            out = new int[1];
            out[0] = (int)Math.round(24 * d);
        }
        return out;
    }
}