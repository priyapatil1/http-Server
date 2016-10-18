package main.responses;

import main.Request;
import main.Response;

import java.util.List;

import static main.Status.METHOD_NOT_ALLOWED;
import static main.Status.OK;

public class EmptyPathResponse extends DefaultResponse {
    private final List<String> content;
    private final String headers;

    public EmptyPathResponse(List content) {
        super(content);
        this.content = content;
        this.headers = "Date: Sun, 18 Oct 2009 08:56:53 GMT\n" +
                       "Server:Apache-HttpClient/4.3.5 (java 1.5)\n" +
                       "ETag: \n" +
                       "Accept-Ranges: none\n" +
                       "Content-Length: \n" +
                       "Connection: close\n" +
                       "Content-Type: text/plain\n";
    }

    @Override
    public Response get(Request request) {
        content.add("<a href=/file1>/file1</a>\n" +
                    "<a href=/file2>/file2</a>\n" +
                    "<a href=/image.gif>/image.gif</a>\n");
        return new Response(OK.get(),
                            headers,
                            body(getBody(content)));
    }

    @Override
    public Response post(Request request) {
        return new Response(METHOD_NOT_ALLOWED.get(),
                headers,
                body(getBody(content)));
    }

    @Override
    public Response head(Request request) {
        return new Response(OK.get(),
                            headers ,
                            body(getBody(content)));
    }

}
