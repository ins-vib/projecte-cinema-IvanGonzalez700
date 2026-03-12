package com.daw.cinemadaw.domain.cinema;

import jakarta.persistence.Column;

public class New {
    
    @Column
    private String headLine;

    @Column
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
