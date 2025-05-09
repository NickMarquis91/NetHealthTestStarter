package client;

import beans.Contact;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import enums.ContactLocation;
import enums.ContactStatus;
import enums.ContactTeam;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class ContactClient {

    private final String url = util.Properties.getUrl();
    private final String requestor = util.Properties.getRequestor();
    private final String subscription = Properties.getSubscription();
    private String authToken;

    public ContactClient (String authToken) {
        this.authToken = authToken;
    }

    /**
     * Execute any tests in sequence, make sure their test case name is defined within method
     */
    public void runAllTests() {
        testConnect();
        testHeist();
        testPowers();
        testBirthdays();
        testSorting();
    }

    public void testConnect() {
        String testCase = "connect";
        List<Contact> contacts = getContacts(testCase).keySet().stream().toList(); // Convert back to a list to post
        System.out.println(postContacts(testCase, contacts));
    }

    public void testHeist() {
        String testCase = "heist";
        List<Contact> badContacts = getContactsByTeam(testCase, ContactTeam.BAD).keySet() // get Bad contacts
                .stream().filter(contact -> !contact.getStatus().equals(ContactStatus.DEAD)).toList(); // remove Dead contacts
        badContacts.stream().forEach(contact -> contact.setLocation(ContactLocation.BANK)); // update location to Bank
        System.out.println(postContacts(testCase, badContacts));
    }

    public void testPowers() {
        String testCase = "powers";
        List<Contact> contacts = new ArrayList<>();
        Contact superman = getContactByTitle(testCase,"Superman");
        superman.getExtraProperties().put("powers", Arrays.asList("Laser Eyes", "X-Ray Vision")); // Superman gets the powers property, with two elements
        contacts.add(superman);
        Contact scarecrow = getContactByTitle("powers","Scarecrow");
        scarecrow.getExtraProperties().put("powers", List.of("Toxic Immunity")); // Scarecrow gets the powers property, with one element
        scarecrow.getExtraProperties().put("abilities", List.of("Pedagogy")); // Scarecrow also gets the abilities property, with one element
        contacts.add(scarecrow);
        System.out.println(postContacts(testCase, contacts));
    }

    public void testBirthdays() {
        String testCase = "birthdays";
        List<Contact> ageContacts = new ArrayList<>(getContacts(testCase).keySet() // get all contacts
                .stream().filter(contact ->
                        !contact.getAge().equals("Unknown") && StringUtils.isNumeric(contact.getAge())).toList()); // remove contacts with Unknown age
                            // Explicitly checking "Unknown" even though isNumeric will catch it just so it's clear why
        ageContacts.sort(Comparator.comparingInt(o -> Integer.parseInt(o.getAge())));
        Contact oldest = ageContacts.getLast();
        // Need the date from today, minus contact's age, to get birthdate
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, Math.negateExact(Integer.parseInt(oldest.getAge())));
        oldest.getExtraProperties().put("birthYear", new SimpleDateFormat("MM-dd-yyyy").format(calendar.getTime()));
        System.out.println(postContacts(testCase, List.of(oldest)));
    }

    public void testSorting() {
        String testCase = "sorting";
        List<Contact> sortedContacts = new ArrayList<>(getContacts(testCase).keySet().stream().toList()); // get all contacts
        sortedContacts.sort(Comparator.comparing(Contact::getTitle)
                .thenComparing(contact -> NumberUtils.toInt(contact.getAge(),0))); // using 0 as default if age unknown/invalid
        System.out.println(postContacts(testCase, sortedContacts));
    }

    /**
     * Shortcut for calling /contacts with no params
     * @param testCase
     * @return
     */
    private Map<Contact,Contact> getContacts(String testCase){
        return getContacts(testCase, new HashMap<String,String>());
    }

    /**
     * Calls /contacts with GET method using the given test case and map of parameters
     * @param testCase
     * @param params
     * @return
     */
    private Map<Contact,Contact> getContacts(String testCase, Map<String,String> params){
        ObjectMapper objectMapper = new ObjectMapper();
        Client client = ClientBuilder.newBuilder().newClient(); // from https://cxf.apache.org/docs/jax-rs-client-api.html
        List<Contact> contacts;
        String paramStr = getParamEncoded(params);
        try {
            WebTarget target = client.target(url+"/contacts"+paramStr);
            Response response = target.request(MediaType.APPLICATION_JSON)
                    .header("Subscription", subscription)
                    .header("authorization", "Bearer "+authToken)
                    .header("requestor", requestor)
                    .header("test_case", testCase)
                    .get();

            // Reading Node->List Pinched from https://stackoverflow.com/questions/17979346/jackson-read-json-in-generic-list
            String responsePayload = response.readEntity(String.class);
            JsonNode payloadRoot = objectMapper.readTree(responsePayload);
            contacts = objectMapper.readerForListOf(Contact.class).with(DeserializationFeature.READ_ENUMS_USING_TO_STRING).readValue(payloadRoot.get("contacts"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Map<Contact,Contact> contactMap = new HashMap<>(); // Make a simple cache from the response that we can operate on easier
        for (Contact contact : contacts) {
            contactMap.put(contact,contact);
        }
        return contactMap;
    }

    /**
     * Get a single contact from the contacts endpoint by their title
     * @param title
     * @return
     */
    private Contact getContactByTitle(String testCase, String title){
        Map<String,String> params = new HashMap<String,String>();
        params.put("title",title);
        return getContacts(testCase, params).get(new Contact(title)); // fetch using the title as the key
    }

    /**
     * Returns a cache of contacts for the given team that can be searched on by title, if needed
     * @param testCase
     * @param team
     * @return
     */
    private Map<Contact, Contact> getContactsByTeam(String testCase, ContactTeam team){
        Map<String,String> params = new HashMap<String,String>();
        params.put("team",team.toString());
        return getContacts(testCase, params);
    }

    /**
     * Calls /contacts with POST method with list of contacts to create/update
     * @param contacts
     * @return
     */
    private List<Contact> postContacts(String testCase, List<Contact> contacts){
        ObjectMapper objectMapper = new ObjectMapper();
        Client client = ClientBuilder.newBuilder().newClient(); // from https://cxf.apache.org/docs/jax-rs-client-api.html
        List<Contact> responseContacts = new ArrayList<>();
        HashMap<String,List<Contact>> contactTree = new HashMap<>(); // easy way to get the list inside a node without using a wrapper class
        contactTree.put("contacts",contacts);
        try {
            WebTarget target = client.target(url+"/contacts");
            Response response = target.request(MediaType.APPLICATION_JSON)
                    .header("Subscription", subscription)
                    .header("authorization", "Bearer "+authToken)
                    .header("requestor", requestor)
                    .header("test_case", testCase)
                    .post(Entity.entity(objectMapper.writer().with(SerializationFeature.WRITE_ENUMS_USING_TO_STRING).writeValueAsString(contactTree),MediaType.APPLICATION_JSON));

            // We won't map the response list, since we won't always need to search on it
            String responsePayload = response.readEntity(String.class);
            JsonNode payloadRoot = objectMapper.readTree(responsePayload);
            responseContacts = objectMapper.readerForListOf(Contact.class).with(DeserializationFeature.READ_ENUMS_USING_TO_STRING).readValue(payloadRoot.get("contacts"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return responseContacts;
    }

    /**
     * Takes params and their values and turns them into a string
     * @param params
     * @return
     */
    private static String getParamEncoded(Map<String, String> params) {
        StringBuffer paramBuf = new StringBuffer();

        // Process the params- if there's more than one, we need to conditionally handle the & separator
        if (!params.isEmpty()) {
            paramBuf.append("?");
            Iterator<Map.Entry<String, String>> paramIterator = params.entrySet().iterator();
            while (paramIterator.hasNext()) {
                Map.Entry<String, String> paramPair = paramIterator.next();
                paramBuf.append(URLEncoder.encode(paramPair.getKey(), StandardCharsets.UTF_8));  // Make sure the str is safe
                paramBuf.append('=');
                paramBuf.append(URLEncoder.encode(paramPair.getValue(), StandardCharsets.UTF_8));
                if (paramIterator.hasNext()) {
                    paramBuf.append('&');
                }
            }
        }
        return paramBuf.toString();
    }
}
