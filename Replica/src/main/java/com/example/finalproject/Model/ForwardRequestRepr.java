package com.example.finalproject.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class ForwardRequestRepr {
    String method;
    String contentType;
    String url;
    String requestURI;
    String queryString;
    String pathInfo;
    String body;

    public ForwardRequestRepr(){};
    @JsonCreator
    public ForwardRequestRepr(
            @JsonProperty("method") String method,
            @JsonProperty("contentType") String contentType,
            @JsonProperty("url") String url,
            @JsonProperty("requestURI") String requestURI,
            @JsonProperty("queryString") String queryString,
            @JsonProperty("pathInfo") String pathInfo,
            @JsonProperty("body") String body) {
        this.method = method;
        this.contentType = contentType;
        this.url = url;
        this.queryString = queryString;
        this.pathInfo = pathInfo;
        this.body = body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ForwardRequestRepr that = (ForwardRequestRepr) o;
        return Objects.equals(method, that.method) && Objects.equals(contentType, that.contentType) && Objects.equals(url, that.url) && Objects.equals(requestURI, that.requestURI) && Objects.equals(queryString, that.queryString) && Objects.equals(pathInfo, that.pathInfo) && Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, contentType, url, requestURI, queryString, pathInfo, body);
    }

    @Override
    public String toString() {
        return "ForwardRequestRepr{" +
                "method='" + method + '\'' +
                ", contentType='" + contentType + '\'' +
                ", url='" + url + '\'' +
                ", requestURI='" + requestURI + '\'' +
                ", queryString='" + queryString + '\'' +
                ", pathInfo='" + pathInfo + '\'' +
                ", body='" + body + '\'' +
                '}';
    }

    public String getRequestURI() {
        return requestURI;
    }

    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public void setPathInfo(String pathInfo) {
        this.pathInfo = pathInfo;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getMethod() {
        return method;
    }

    public String getContentType() {
        return contentType;
    }

    public String getUrl() {
        return url;
    }

    public String getQueryString() {
        return queryString;
    }

    public String getPathInfo() {
        return pathInfo;
    }

    public String getBody() {
        return body;
    }
}
