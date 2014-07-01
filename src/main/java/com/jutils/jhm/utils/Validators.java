/* 
 * @(#)Validators.java    Created on 2014年7月1日
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.jutils.jhm.utils;

/**
 * @author jianghm
 * @version $Revision: 1.0 $, $Date: 2014年7月1日 下午4:54:08 $
 */
public class Validators {

    /** 手机号码的正则表达式 */
    private static final String REGEX_MOBILE = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
    /** 身份证的正则表达式 */
    private static final String REGEX_IDCARD = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}(\\d|X)$";
    /** 车牌的正则表达式 */
    private static final String REGEX_PLATENUMBER = "^[\u4e00-\u9fa5|WJ]{1}[A-Z0-9]{6}$";
    /** 电子邮箱的正则表达式 */
    private static final String REGEX_EMAIL = ".+@.+\\.[a-z]+";
    /** 电话号码的正则表达式 */
    private static final String REGEX_PHONE_NUMBER = "(([\\(（]\\d+[\\)）])?|(\\d+[-－]?)*)\\d+";
    /** 邮政编码的正则表达式 */
    private static final String REGEX_ZIP = "[1-9]\\d{5}(?!\\d)";
    /** ip4v的正则表达式 */
    private static final String REGEX_IPV4 = "\\d{1,3}+\\.\\d{1,3}+\\.\\d{1,3}+\\.\\d{1,3}";

    /**
     * 判断字符串是否匹配了正则表达式。
     * 
     * @param str
     *            字符串
     * @param regex
     *            正则表达式
     * @return true/false
     */
    public static boolean isRegexMatch(String str, String regex) {
        return str != null && str.matches(regex);
    }

    /**
     * 判断手机号码格式
     * 
     * @param str
     * @return true/false
     */
    public static boolean isMobile(String str) {
        return isRegexMatch(str, REGEX_MOBILE);
    }

    /**
     * 判断身份证格式
     * 
     * @param str
     * @return true/false
     */
    public static boolean isIdCard(String str) {
        return isRegexMatch(str, REGEX_IDCARD);
    }

    /**
     * 判断车牌号格式
     * 
     * @param str
     * @return true/false
     */
    public static boolean isPlateNumber(String str) {
        return isRegexMatch(str, REGEX_PLATENUMBER);
    }

    /**
     * 判断电子邮箱格式
     * 
     * @param str
     * @return true/false
     */
    public static boolean isEmail(String str) {
        return isRegexMatch(str, REGEX_EMAIL);
    }

    /**
     * 判断电话号码格式
     * 
     * @param str
     * @return true/false
     */
    public static boolean isPhoneNumber(String str) {
        return isRegexMatch(str, REGEX_PHONE_NUMBER);
    }

    /**
     * 判断邮政编码格式
     * 
     * @param str
     * @return true/false
     */
    public static boolean isZip(String str) {
        return isRegexMatch(str, REGEX_ZIP);
    }

    /**
     * 判断ipv4格式
     * 
     * @param str
     * @return true/false
     */
    public static boolean isIPV4(String str) {
        return isRegexMatch(str, REGEX_IPV4);
    }

}
