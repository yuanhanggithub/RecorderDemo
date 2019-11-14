package com.police.entity;

/**
 * Created by jsjm on 2018/11/27.
 */

public class CallPhoneEntity {
    String title;
    String desc;

    public CallPhoneEntity(String title, String desc) {
        this.title = title;
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setdesc(String desc) {
        this.desc = desc;
    }


}
