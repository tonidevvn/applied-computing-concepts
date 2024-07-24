package ca.uwindsor.appliedcomputing.final_project.util;

public class ValidatorUtil {
    private static final String URL_REGEX =
        "^((http|https)://)?(www\\.)?[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}(:[0-9]{1,5})?(/.*)?$";

    public static boolean isValidHtmlUrl(String url) {
        return url != null && url.matches(URL_REGEX);
    }
}
