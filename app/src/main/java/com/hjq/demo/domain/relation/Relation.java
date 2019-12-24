package com.hjq.demo.domain.relation;

import java.util.List;

public class Relation {

    //人与人之间的关系
    private Long id = 1L;
    private String friend = "123";
    private String memo = "";
    private Integer group = 10;
    private String recordlist = "123";

    private List<Record>recordListObj;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getGroup() {
        return group;
    }

    public void setGroup(Integer group) {
        this.group = group;
    }

    public String getRecordlist() {
        return recordlist;
    }

    public void setRecordlist(String recordlist) {
        this.recordlist = recordlist;
    }

    public List<Record> getRecordListObj() {
        return recordListObj;
    }

    public void setRecordListObj(List<Record> recordListObj) {
        this.recordListObj = recordListObj;
    }
}
