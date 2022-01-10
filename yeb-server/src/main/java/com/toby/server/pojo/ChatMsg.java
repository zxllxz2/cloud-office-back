package com.toby.server.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 聊天消息
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ChatMsg {

    /**
     * 发送者用户名
     */
    private String from;

    /**
     * 接收者用户名
     */
    private String to;

    /**
     * 信息内容
     */
    private String content;

    /**
     * 发送时间
     */
    private LocalDateTime date;

    /**
     * 发送者昵称
     */
    private String fromNickName;

}
