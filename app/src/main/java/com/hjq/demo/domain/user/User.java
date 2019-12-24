package com.hjq.demo.domain.user;

import com.hjq.demo.domain.relation.Moment;
import com.hjq.demo.domain.relation.Relation;

import java.util.List;

public class User {

    //用户
    private Long id = 1L;
    private String username = "PMaS";
    private String passward = "PMaS";
    private Integer active = 10;
    private Integer authority = 15;

    private String value;   //字符串列表

    //对应的保存id的列表和id对应的对象列表(字符串)
    private String momentlist = "123";
    private String relationlist = "123";
    private String requestsendlist = "123";
    private String requestgetlist = "123";
    private String personinform = "123";

    private List<Moment>momentListObj;
    private List<Relation>relationListObj;
    private List<Request>requestSendListObj;
    private List<Request>requestGetListObj;
    private Info personinformObj;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassward() {
        return passward;
    }

    public void setPassward(String passward) {
        this.passward = passward;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public Integer getAuthority() {
        return authority;
    }

    public void setAuthority(Integer authority) {
        this.authority = authority;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMomentlist() {
        return momentlist;
    }

    public void setMomentlist(String momentlist) {
        this.momentlist = momentlist;
    }

    public String getRelationlist() {
        return relationlist;
    }

    public void setRelationlist(String relationlist) {
        this.relationlist = relationlist;
    }

    public String getRequestsendlist() {
        return requestsendlist;
    }

    public void setRequestsendlist(String requestsendlist) {
        this.requestsendlist = requestsendlist;
    }

    public String getRequestgetlist() {
        return requestgetlist;
    }

    public void setRequestgetlist(String requestgetlist) {
        this.requestgetlist = requestgetlist;
    }

    public String getPersoninform() {
        return personinform;
    }

    public void setPersoninform(String personinform) {
        this.personinform = personinform;
    }

    public List<Moment> getMomentListObj() {
        return momentListObj;
    }

    public void setMomentListObj(List<Moment> momentListObj) {
        this.momentListObj = momentListObj;
    }

    public List<Relation> getRelationListObj() {
        return relationListObj;
    }

    public void setRelationListObj(List<Relation> relationListObj) {
        this.relationListObj = relationListObj;
    }

    public List<Request> getRequestSendListObj() {
        return requestSendListObj;
    }

    public void setRequestSendListObj(List<Request> requestSendListObj) {
        this.requestSendListObj = requestSendListObj;
    }

    public List<Request> getRequestGetListObj() {
        return requestGetListObj;
    }

    public void setRequestGetListObj(List<Request> requestGetListObj) {
        this.requestGetListObj = requestGetListObj;
    }

    public Info getPersoninformObj() {
        return personinformObj;
    }

    public void setPersoninformObj(Info personinformObj) {
        this.personinformObj = personinformObj;
    }
}
