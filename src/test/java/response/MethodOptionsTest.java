package response;

import main.Request;
import main.responses.MethodOptionsResponse;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class MethodOptionsTest {

    private final TestHelper helper = new TestHelper();
    private final List content = new ArrayList<>();
    private MethodOptionsResponse response;
    private Request getOptions;
    private Request putOptions;
    private Request postOptions;
    private Request optionsOptions;
    private Request deleteOptions;

    @Before
    public void setUp() {
        response = new MethodOptionsResponse(content);
        getOptions = helper.create("GET /method_options");
        putOptions = helper.create("PUT /method_options");
        postOptions = helper.create("POST /method_options");
        optionsOptions = helper.create("OPTIONS /method_options");
        deleteOptions = helper.create("DELETE /method_options");
    }

    @Test
    public void responseForGetMethodOptions() {
        String createdResponse = response.get(getOptions);
        assertEquals("HTTP/1.1 200 OK\n" +
                     "Date: Sun, 18 Oct 2009 08:56:53 GMT\n" +
                     "Server:Apache-HttpClient/4.3.5 (java 1.5)\n" +
                     "ETag: \n" +
                     "Accept-Ranges: none\n" +
                     "Content-Length: \n" +
                     "Connection: close\n" +
                     "Content-Type: text/plain\n\n\n", createdResponse);
    }

    @Test
    public void responseForPutMethodOptions() {
        String createdResponse = response.put(putOptions);
        assertThat(createdResponse, containsString("HTTP/1.1 200 OK\n"));
    }

    @Test
    public void responseForPostMethodOptions() {
        String createdResponse = response.post(postOptions);
        assertThat(createdResponse, containsString("HTTP/1.1 200 OK\n"));
    }

    @Test
    public void responseForHeadMethodOptions() {
        String createdResponse = response.head(postOptions);
        assertThat(createdResponse, containsString("HTTP/1.1 200 OK\n"));
    }

    @Test
    public void responseForDeleteMethodOptions() {
        String createdResponse = response.delete(deleteOptions);
        assertEquals("HTTP/1.1 405 Method Not Allowed\n" +
                     "Date: Sun, 18 Oct 2009 08:56:53 GMT\n" +
                     "Server:Apache-HttpClient/4.3.5 (java 1.5)\n" +
                     "ETag: \n" +
                     "Accept-Ranges: none\n" +
                     "Content-Length: \n" +
                     "Connection: close\n" +
                     "Content-Type: text/plain\n\n\n", createdResponse);
    }

    @Test
    public void allowIncludedInMethodOptionsRequest() {
        String createdResponse = response.options(optionsOptions);
        assertThat(createdResponse, containsString("HTTP/1.1 200 OK\n"));
        assertThat(createdResponse, containsString("Allow: GET,HEAD,POST,OPTIONS,PUT\n"));
    }
}