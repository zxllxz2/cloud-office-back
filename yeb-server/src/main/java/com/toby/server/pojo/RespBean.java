package com.toby.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 公共返回对象
 *
 * @author Xiaoliang Zhu
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespBean {

    private long code;
    private String message;
    private Object obj;

    /**
     * return successfully
     * @param message
     * @return
     */
    public static RespBean success(String message) {
        return new RespBean(200, message, null);
    }

    /**
     * return successfully with object
     * @param message
     * @return
     */
    public static RespBean success(String message, Object object) {
        return new RespBean(200, message, object);
    }

    /**
     * fail to return
     * @param message
     * @return
     */
    public static RespBean error(String message) {
        return new RespBean(500, message, null);
    }

    /**
     * fail to return with object
     * @param message
     * @return
     */
    public static RespBean error(String message, Object object) {
        return new RespBean(500, message, object);
    }

}
