package com.etv.util.permission;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/10 0010.
 */

public interface PermissionCallback extends Serializable {
    /***
     * 权限通过
     */
    void onFinish();

    /***
     * 权限没有通过
     */
    void notAllow();
}