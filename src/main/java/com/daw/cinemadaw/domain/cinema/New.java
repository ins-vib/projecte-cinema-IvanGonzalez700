package com.daw.cinemadaw.domain.cinema;

public class New {
    
    private String headLine;
    private String body;
    
    public New(String headLine, String body) {
        this.headLine = headLine;
        this.body = body;
    }

    public String getHeadLine() {
        return headLine;
    }

    public void setHeadLine(String headLine) {
        this.headLine = headLine;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    
}
