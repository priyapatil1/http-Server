package main;

import main.responses.*;
import main.responses.ResourceResponse;

import java.io.File;
import java.util.*;

public class ResponseFactory {
    private final HashMap<String, DefaultResponse> responses;
    String publicDirectory = "/Users/priyapatil/cob_spec/public";

    public ResponseFactory(List content) {
        this.responses = new HashMap<>();
        responses.put("/", new EmptyPathResponse(content));
        responses.put("/form", new FormResponse(content));
        responses.put("/method_options", new MethodOptionsResponse(content));
        responses.put("/method_options2", new MethodOptions2Response(content));
        responses.put("/coffee", new CoffeeResponse(content));
        responses.put("/tea", new TeaResponse(content));
        responses.put("/redirect", new RedirectResponse(content));
        responses.put("resource", new ResourceResponse(publicDirectory, content));
        responses.put("no resource", new NoResourceResponse(content));
    }

    public DefaultResponse findRelevantResponse(Request request) {
        if (exists(request.getPath()) && (request.getRequestMethod().equals("GET"))) {
            return responses.get("resource");
        }
        for (Map.Entry<String, DefaultResponse> path : responses.entrySet()) {
            if (path.getKey().equals(request.getPath())) {
               return path.getValue();
            }
        }
        return responses.get("no resource");
    }

    private boolean exists(String filePathToFind) {
        File fileToFind = new File(publicDirectory + filePathToFind);
        return getFiles().stream()
                .filter(fileToFind::equals)
                .findAny()
                .isPresent();
    }

    public List<File> getFiles() {
        File[] files = new File(publicDirectory).listFiles();
        return new ArrayList<>(Arrays.asList(files));
    }
}
