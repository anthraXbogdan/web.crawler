package links.parsing;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebsiteTitleGetter  {

    public static String getTitle(String url) {
        String title = "";
        try (InputStream inputStream = new BufferedInputStream(new URL(url).openStream())) {

            String siteText = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            Pattern pattern = Pattern.compile("<title>.*(\\s)?.*</title>");
            Matcher matcher = pattern.matcher(siteText);

            if (matcher.find()) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = matcher.start() + 7; i < matcher.end() - 8; i++) {
                    stringBuilder.append(siteText.charAt(i));
                }
                title = stringBuilder.toString();
            }
        } catch (IOException ignored) {

        }
        return title;
    }

}
