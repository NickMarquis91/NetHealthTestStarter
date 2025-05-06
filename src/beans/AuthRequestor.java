package beans;

public class AuthRequestor {

    String authentication;
    String requestor;

    public AuthRequestor(String authentication, String requestor) {
        this.authentication = authentication;
        this.requestor = requestor;
    }

    public String getAuthentication() {
        return authentication;
    }

    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }

    public String getRequestor() {
        return requestor;
    }

    public void setRequestor(String requestor) {
        this.requestor = requestor;
    }
}
