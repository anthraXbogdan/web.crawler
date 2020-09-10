package links.parsing;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkValidityChecker {

    public static boolean checker(String link) throws NullPointerException {

        boolean isTextLink = false;

        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.connect();
            String contentType = connection.getContentType();
            Pattern pattern = Pattern.compile("text/html");
            if (contentType != null) {
                Matcher matcher = pattern.matcher(contentType);
                if (matcher.find()) {
                    isTextLink = true;
                }
            }

            //connection.disconnect();
        } catch (IOException ignored) {
        }
        return isTextLink;
    }

}
