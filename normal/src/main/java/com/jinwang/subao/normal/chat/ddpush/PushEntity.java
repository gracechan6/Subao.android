package com.jinwang.subao.normal.chat.ddpush;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by dreamy on 2015/7/9.
 */
public class PushEntity {
    @JsonProperty("from")
    private String from;
    @JsonProperty("to")
    private String to;
    @JsonProperty("message")
    private String message;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getContent() {
        return message;
    }

    public void setContent(String content) {
        this.message = content;
    }
}
