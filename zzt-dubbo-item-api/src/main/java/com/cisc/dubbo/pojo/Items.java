package com.cisc.dubbo.pojo;

public class Items {
    private String id;

    private String name;

    private Integer counts;

    private Integer buycounts;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getCounts() {
        return counts;
    }

    public void setCounts(Integer counts) {
        this.counts = counts;
    }

    public Integer getBuycounts() {
        return buycounts;
    }

    public void setBuycounts(Integer buycounts) {
        this.buycounts = buycounts;
    }
}