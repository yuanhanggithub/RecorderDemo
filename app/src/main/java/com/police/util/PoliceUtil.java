package com.police.util;

import com.police.entity.CallPhoneEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jsjm on 2018/11/27.
 */

public class PoliceUtil {

    public static List<CallPhoneEntity> getData() {
        List<CallPhoneEntity> lists = new ArrayList<>();
        lists.add(new CallPhoneEntity("1", "Ω"));
        lists.add(new CallPhoneEntity("2", "ABC"));
        lists.add(new CallPhoneEntity("3", "DEF"));
        lists.add(new CallPhoneEntity("4", "GHI"));
        lists.add(new CallPhoneEntity("5", "JLK"));
        lists.add(new CallPhoneEntity("6", "MNO"));
        lists.add(new CallPhoneEntity("7", "PQRS"));
        lists.add(new CallPhoneEntity("8", "TUV"));
        lists.add(new CallPhoneEntity("9", "WXYZ"));
        lists.add(new CallPhoneEntity("*", "(P)"));
        lists.add(new CallPhoneEntity("0", "+"));
        lists.add(new CallPhoneEntity("#", "(W)"));
        return lists;
    }


}
