package links.parsing;

public class SlashChecker {

    public static boolean check(String rawLink) {
        boolean hasSlashes = false;
        char[] list = rawLink.toCharArray();
        for (char c : list) {
            if (c == '/') {
                hasSlashes = true;
                break;
            }
        }
        return hasSlashes;
    }
}
