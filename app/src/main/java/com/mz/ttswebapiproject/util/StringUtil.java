package com.mz.ttswebapiproject.util;

import java.util.UUID;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2020/12/21 15:54
 * @Description 文件描述：
 */
public class StringUtil {

    public static String getUUID() {
        StringBuffer uuid = new StringBuffer();
        String strs[] = UUID.randomUUID().toString().split("-");
        // 去掉“-”符号
        for (String s : strs) {
            uuid.append(s);

        }
        return uuid.toString();
    }
}
