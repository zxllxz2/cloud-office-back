package com.toby.server.config;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.toby.server.pojo.MailConstants;
import com.toby.server.pojo.MailLog;
import com.toby.server.service.IMailLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置类
 */
@Configuration
public class RabbitMQConfig {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConfig.class);

    @Autowired
    private CachingConnectionFactory connectionFactory;

    @Autowired
    private IMailLogService mailLogService;

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        /**
         * 消息确认回调，确认消息是否到达broker
         * @param data  消息唯一标识
         * @param ack   确认结果
         * @param cause 失败原因
         */
        rabbitTemplate.setConfirmCallback((data, ack, cause) -> {
            String msgId = data.getId();
            if (ack) {
                LOGGER.info("{} ============> Message is sent successfully!", msgId);
                mailLogService.update(new UpdateWrapper<MailLog>().set("status", 1)
                        .eq("msgId", msgId));
            } else {
                LOGGER.error("{} ============> Fail to send the message!", msgId);
            }
        });
        /**
         * 消息失败回调，例如router不到queue时回调
         * @param msg         消息主题
         * @param repCode     响应码
         * @param repText     相应描述
         * @param exchange    交换机
         * @param routingKey  路由键
         *
         */
        rabbitTemplate.setReturnCallback((msg, repCode, repText, exchange, routingKey) -> {
            LOGGER.error("{} ============> Fail to send the message when sending to the queue!",
                    msg.getBody());
        });

        return rabbitTemplate;
    }

    @Bean
    public Queue queue() {
        return new Queue(MailConstants.MAIL_QUEUE_NAME);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(MailConstants.MAIL_EXCHANGE_NAME);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(directExchange()).with(MailConstants.MAIL_ROUTING_KEY_NAME);
    }

}
