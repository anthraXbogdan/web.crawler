package links.parsing;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkTitleReturner {

    public static String returner(String link) {
        String linkTitle = "";
        Thread t1 = new Thread();
        try (InputStream inputStream = new BufferedInputStream(new URL(link).openStream())) {
            String siteText = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            Pattern title = Pattern.compile("<title>.*(\\s)?.*</title>");
            Matcher matcher2 = title.matcher(siteText);

            if (matcher2.find()) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = matcher2.start() + 7; i < matcher2.end() - 8; i++) {
                    stringBuilder.append(siteText.charAt(i));
                }
                linkTitle = stringBuilder.toString();
            }
        } catch (IOException ignored) {
        }
        return linkTitle;
    }

}
