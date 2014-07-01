/* 
 * @(#)ObjectUtil.java    Created on 2014年7月1日
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.jutils.jhm.utils;

/**
 * @author jianghm
 * @version $Revision: 1.0 $, $Date: 2014年7月1日 下午4:52:25 $
 */
public class ObjectUtils {

    /**
     * 反射，根据当前传入对象实例，属性名，返回执行后的值
     * 
     * @param obj
     * @param fieldName
     * @return
     */
    public static Object getProperty(Object obj, String fieldName) {
        if (obj == null || fieldName == null) {
            return null;
        }
        String m = fieldName.substring(0, 1).toUpperCase();
        m = "get" + m + fieldName.substring(1, fieldName.length());
        try {
            return obj.getClass().getMethod(m, new Class[0]).invoke(obj);
        }
        catch (Exception e) {
            m = fieldName.substring(0, 1).toUpperCase();
            m = "is" + m + fieldName.substring(1, fieldName.length());
            try {
                return obj.getClass().getMethod(m, new Class[0]).invoke(obj);
            }
            catch (Exception e1) {
            }
        }
        return null;
    }

}
