package com.easyshop.util;

import java.util.UUID;
/**
 * Created by pavan on 2/5/17.
 */
public class EasyShopUtil {
    public static String getRandonUDID(){
        UUID id = UUID.randomUUID();
        return id.toString();
    }
}
