package not.mepipe.meconomy.utils;

public class Helpers {

    public static int parseInt(String stringDouble) {
        double d = Double.parseDouble(stringDouble);
        return (int) d;
    }

    public static String concatenate(String[] s, int start, int end, String append)
    {
        StringBuilder string = new StringBuilder();
        for (int i = start; i < end; i++) {
            string.append(s[i]).append(append);
        }
        string.append(s[end]);
        return string.toString();
    }

    public static String getTime(long timeMillis, int timezoneOffset) {
        long timeSeconds = timeMillis / 1000;

        int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        long daysTillNow = timeSeconds / 86400;
        long extraTime = timeSeconds % 86400;
        int currYear = 1970;

        // Calculating current year
        while (true) {
            if (currYear % 400 == 0 || (currYear % 4 == 0 && currYear % 100 != 0)) {
                if (daysTillNow < 366) {
                    break;
                }
                daysTillNow -= 366;
            } else {
                if (daysTillNow < 365) {
                    break;
                }
                daysTillNow -= 365;
            }
            currYear++;
        }

        long extraDays = daysTillNow + 1; // Includes current day (+ 1)

        boolean isLeapYear = currYear % 400 == 0 || (currYear % 4 == 0 && currYear % 100 != 0);

        // Calculating Month and Date
        int month = 0;
        for (int i = 0; i < 12; i++) {
            if (i == 1 && isLeapYear) {
                if (extraDays - 29 < 0) {
                    break;
                }
                month++;
                extraDays -= 29;
            } else {
                if (extraDays - daysOfMonth[i] < 0) {
                    break;
                }
                month++;
                extraDays -= daysOfMonth[i];
            }
        }

        long date;
        // Current Month
        if (extraDays > 0) {
            month++;
            date = extraDays;
        } else if (month == 2 && isLeapYear) {
            date = 29;
        } else {
            date = daysOfMonth[month - 1];
        }

        // Calculating HH:MM:YYYY
        long hours = (extraTime / 3600) + timezoneOffset;
        long minutes = (extraTime % 3600) / 60;
        long seconds = (extraTime % 3600) % 60;

        if (hours < 0) {
            date--;
            hours = 24 + ((hours - timezoneOffset) + timezoneOffset);
            if (date < 1) {
                month--;
                if (month < 1) {
                    month = 12;
                    currYear--;
                }
                date = daysOfMonth[month - 1];
            }
        } else if (hours > 23) {
            date++;
            hours = timezoneOffset - 12;
            if (date > daysOfMonth[month - 1] || ((month == 2 && isLeapYear) && date > 29)) {
                date = 1;
                month++;
                if(month > 12) {
                    currYear++;
                    month = 1;
                }
            }
        }

        return formatDateAndTime(month, date, currYear, hours, minutes, seconds);
    }

    private static String formatDateAndTime(int month, long date, int year, long hour, long min, long sec) {
        String monthString = String.valueOf(month);
        String dateString = String.valueOf(date);
        String yearString = String.valueOf(year).substring(2);
        String hourString = String.valueOf(hour);
        String minuteString = String.valueOf(min);
        String secondString = String.valueOf(sec);
        if (month / 10 == 0) {
            monthString = "0" + month;
        }
        if (date / 10 == 0) {
            dateString = "0" + date;
        }
        if (hour / 10 == 0) {
            hourString = "0" + hour;
        }
        if (min / 10 == 0) {
            minuteString = "0" + min;
        }
        if (sec / 10 == 0) {
            secondString = "0" + sec;
        }

        String dmyString = monthString + "/" + dateString + "/" + yearString;
        String hmsString = hourString + ":" + minuteString + ":" + secondString;

        return dmyString + " " + hmsString;
    }
}
