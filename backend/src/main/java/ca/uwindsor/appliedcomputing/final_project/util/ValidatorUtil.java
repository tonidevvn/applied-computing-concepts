package ca.uwindsor.appliedcomputing.final_project.util;

public class ValidatorUtil {
    public static boolean isValidHtmlUrl(String url) {
        return url != null && url.matches("^(http|https)://.*.html$");
    }
}
