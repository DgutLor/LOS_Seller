package com.wsns.lor.seller.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


/**
 * 自家服务器分页数据封装
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Page<T> {
    List<T> content;
    Integer number;
    Integer totalPages;


    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }


}