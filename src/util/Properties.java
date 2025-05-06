package util;

public abstract class Properties {

    public static String getUrl() {
       return System.getenv("URL");
    }

    public static String getRequestor() {
        return System.getenv("REQUESTOR");
    }

    public static String getSubscription() {
        return System.getenv("SUBSCRIPTION");
    }

    public static String getClientId() {
        return System.getenv("CLIENT_ID");
    }

    public static String getClientSecret() {
        return System.getenv("CLIENT_SECRET");
    }

}
