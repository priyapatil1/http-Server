package main;

public enum Status {
    OK("200 OK"),
    NOT_FOUND("404 Not Found"),
    METHOD_NOT_ALLOWED("405 Method Not Allowed"),
    REDIRECT("302 Redirect"),
    PARTIAL("206 Partial"),
    NO_CONTENT("204 No Content"),
    NOT_AUTHORIZED("401 Not Authorized"),
    COFFEE("418 I'm a teapot");

    private String status;

    Status(String status) {
        this.status = status;
    }

    public String get() {
        return "HTTP/1.1 " + status;
    }
}

