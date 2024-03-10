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
}
