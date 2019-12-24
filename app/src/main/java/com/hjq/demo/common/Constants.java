package com.hjq.demo.common;

import com.hjq.demo.domain.activity.SocialActivity;
import com.hjq.demo.domain.relation.Moment;
import com.hjq.demo.domain.relation.Relation;
import com.hjq.demo.domain.user.Info;
import com.hjq.demo.domain.user.Request;
import com.hjq.demo.domain.user.User;

import java.util.List;

public class Constants {

    //constants in this project

    //服务器资源地址信息
    public static String serverUrl = "http://47.97.175.189:8080/Entity/U13bc7f101f05da/pmas/";

    //服务器文件地址信息
    public static String fileUrl = "http://47.97.175.189:8080/File/U13bc7f101f05da/pmas/";

    //用户信息
    public static User user;

    //用户详细信息
    public static Info info;

    //好友list
    public static List<User>friends;

    //关系list
    public static List<Relation>relations;

    //活动列表
    public static List<SocialActivity>socialActivityList;

    //动态列表
    public static List<Moment>momentList;

    //请求列表
    public static  List<Request>requestList;
}
