package client;

import beans.AuthRequestor;
import beans.AuthResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Base64;

public class AuthClient {

    private final String url = Properties.getUrl();
    private final String requestor = Properties.getRequestor();
    private final String subscription = Properties.getSubscription();
    private final String clientId = Properties.getClientId();
    private final String clientSecret = Properties.getClientSecret();
    private final String authenticationString = getAuthenticationString(clientId, clientSecret);

    /**
     * Use env variables to get auth token as a plain string
     * @return
     */
    public String getAuthToken () {
        ObjectMapper objectMapper = new ObjectMapper();
        Client client = ClientBuilder.newBuilder().newClient(); // from https://cxf.apache.org/docs/jax-rs-client-api.html
        String authToken;
        try {
            WebTarget target = client.target(url+"/auth"); // you could also use URLBuilder here
            Response response = target.request(MediaType.APPLICATION_JSON)
                    .header("Subscription", subscription)
                    .post(Entity.entity(objectMapper.writeValueAsString(new AuthRequestor(authenticationString, requestor)),MediaType.APPLICATION_JSON));

            AuthResponse authResponse = objectMapper.readValue(response.readEntity(String.class), AuthResponse.class);
            authToken = authResponse.getAccessToken();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return authToken;
    }

    /**
     * Needs to be in Base64, with a colon between client id and secret
     * @param clientId
     * @param clientSecret
     * @return
     */
    private static String getAuthenticationString(String clientId, String clientSecret) {
        return Base64.getEncoder().encodeToString((clientId+":"+clientSecret).getBytes());
    }
}
