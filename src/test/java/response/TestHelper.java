package response;

import main.request.Request;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

public class TestHelper {
    private final static String header = "Host: localhost:5000\n" +
                                         "Connection: Keep-Alive\n" +
                                         "User-Agent: Apache-HttpClient/4.3.5 (java 1.5)\n" +
                                         "Accept-Encoding: gzip,deflate";


    public Request create(String requestLine) {
        String requestContent = requestLine + " HTTP/1.1\n" +
                                header;
        BufferedReader reader = createBufferedReader(requestContent);
        return new Request(reader);
    }

    public Request createPartial(String requestLine, int size) {
        String requestContent = requestLine + " HTTP/1.1\n" +
                "Host: localhost:5000\n" +
                "Connection: Keep-Alive\n" +
                "Range: bytes=0-"+ size + "\n" +
                "User-Agent: Apache-HttpClient/4.3.5 (java 1.5)\n" +
                "Accept-Encoding: gzip,deflate";
        BufferedReader reader = createBufferedReader(requestContent);
        return new Request(reader);
    }

    private BufferedReader createBufferedReader(String request) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(request.getBytes());
        return new BufferedReader(new InputStreamReader(inputStream));
    }

    public Request createPartialEnd(String requestLine, int size) {
        String requestContent = requestLine + " HTTP/1.1\n" +
                "Host: localhost:5000\n" +
                "Connection: Keep-Alive\n" +
                "Range: bytes=-"+ size + "\n" +
                "User-Agent: Apache-HttpClient/4.3.5 (java 1.5)\n" +
                "Accept-Encoding: gzip,deflate";
        BufferedReader reader = createBufferedReader(requestContent);
        return new Request(reader);
    }

    public Request createPartialBeginning(String requestLine, int size) {
        String requestContent = requestLine + " HTTP/1.1\n" +
                "Host: localhost:5000\n" +
                "Connection: Keep-Alive\n" +
                "Range: bytes="+ size + "-\n" +
                "User-Agent: Apache-HttpClient/4.3.5 (java 1.5)\n" +
                "Accept-Encoding: gzip,deflate";
        BufferedReader reader = createBufferedReader(requestContent);
        return new Request(reader);
    }
}
