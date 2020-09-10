package links.parsing;

public class LinkTrimming {

    public static String trimming(String absoluteLink) {
        char[] list = absoluteLink.toCharArray();

        for (int i = absoluteLink.length() - 1; i >= 0; i--) {
            if (list[i] != '/') {
                list[i] = ' ';
            }
            if (list[i] == '/') {
                break;
            }
        }

        StringBuilder stringBuilder1 = new StringBuilder();

        for (char c : list) {
            stringBuilder1.append(c);
        }
        String trimmedLink = stringBuilder1.toString().strip();

        StringBuilder stringBuilder2 = new StringBuilder();

        for (int i = 0; i < trimmedLink.length() - 1; i ++) {
            stringBuilder2.append(trimmedLink.charAt(i));
        }
        return stringBuilder2.toString();
    }
}
