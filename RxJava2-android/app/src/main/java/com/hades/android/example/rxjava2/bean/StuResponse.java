package com.hades.android.example.rxjava2.bean;

public class StuResponse {
    private String name;
    private Integer score;

    public StuResponse(String name, Integer score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public Integer getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "StuResponse{" +
                "name='" + name + '\'' +
                ", score=" + score +
                '}';
    }
}
