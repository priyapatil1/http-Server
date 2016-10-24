package main.responsetypes;

import main.date.Date;
import main.response.DefaultHeaders;
import main.request.Request;
import main.response.Response;

import java.util.List;

import static main.Status.OK;

public class FormResponse extends DefaultResponse {
    private final List<String> content;
    private final String headers;

    public FormResponse(List<String> content, Date date) {
        super(content, date);
        this.content = content;
        this.headers = new DefaultHeaders(date).get();
    }

    @Override
    public Response get(Request request) {
        return new Response(OK.get(),
                            headers,
                            convertToBytes(getBody(content)));
    }

    @Override
    public Response post(Request request) {
        content.add(0, "\ndata=fatcat");
        return new Response(OK.get(),
                            headers,
                            convertToBytes(getBody(content)));
    }

    @Override
    public Response put(Request request) {
        removePreviousData();
        content.add(0, "\ndata=heathcliff");
        return new Response(OK.get(),
                            headers,
                            convertToBytes(getBody(content)));
    }

    @Override
    public Response delete(Request request) {
        removePreviousData();
        return new Response(OK.get(),
                            headers,
                            convertToBytes(getBody(content)));
    }

    private void removePreviousData() {
        if (content.size() > 0) {
            content.removeAll(content);
        }
    }
}