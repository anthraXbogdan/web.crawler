package links.parsing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProtocolSetter {

    public static String setProtocol(String url) {
        String protocol = "";

        Pattern protocolPattern1 = Pattern.compile("^http://");
        Matcher protocolMatcher1 = protocolPattern1.matcher(url);

        Pattern protocolPattern2 = Pattern.compile("^https://");
        Matcher protocolMatcher2 = protocolPattern2.matcher(url);

        if (protocolMatcher1.find()) {
            protocol = "http:";
        }
        else if (protocolMatcher2.find()) {
            protocol = "https:";
        } else {
            protocol = "";
        }
        return protocol;
    }

}
