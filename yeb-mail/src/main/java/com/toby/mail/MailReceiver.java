package com.toby.mail;

import com.rabbitmq.client.Channel;
import com.toby.server.pojo.Employee;
import com.toby.server.pojo.MailConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;

/**
 * 接受邮件
 */
@Component
public class MailReceiver {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MailReceiver.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MailProperties mailProperties;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private RedisTemplate redisTemplate;

    @RabbitListener(queues = MailConstants.MAIL_QUEUE_NAME)
    public void handler(Message message, Channel channel) {
        Employee employee = (Employee) message.getPayload();
        MessageHeaders headers = message.getHeaders();
        //判断消费端幂等性（如何处理两条id一样的消息的情况
        //处理方法：发送时在redis中查询是否有id相同的消息，若无则将消息id存入redis并发送消息
        long tag = (long) headers.get(AmqpHeaders.DELIVERY_TAG);        //消息序号
        String msgId = (String) headers.get("spring_returned_message_correlation");
        HashOperations ops = redisTemplate.opsForHash();
        try {
            if (ops.entries("mail_log").containsKey(msgId)) {
                LOGGER.error("The message has already been consumed! ============> {}", msgId);
                /**
                 * 手动确认消息
                 * @param tag         消息序号
                 * @param b,multiple  是否确认多条消息
                 */
                channel.basicAck(tag, false);
                return;
            }
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            //发件人
            helper.setFrom(mailProperties.getUsername());
            //收件人
            helper.setTo(employee.getEmail());
            //主题
            helper.setSubject("Welcome to the family!");
            //发送日期
            helper.setSentDate(new Date());
            //邮件内容
            Context context = new Context();
            context.setVariable("name", employee.getName());
            context.setVariable("posName", employee.getPosition().getName());
            context.setVariable("jobLevelName", employee.getJoblevel().getName());
            context.setVariable("depName", employee.getDepartment().getName());
            //生成邮件
            String mail = templateEngine.process("mail", context);
            helper.setText(mail, true);
            //发送邮件
            javaMailSender.send(mimeMessage);
            LOGGER.info("The email is sent successfully!");
            //将消息id存入redis
            ops.put("mail_log", msgId, "OK");
            //手动确认消息
            channel.basicAck(tag, false);
        } catch (Exception e) {
            /**
             * 手动确认消息
             * @param tag         消息序号
             * @param b,multiple  是否确认多条消息
             * @param b1,requeue  是否退回到队列
             */
            try {
                channel.basicNack(tag, false, true);
            } catch (IOException ex) {
                LOGGER.error("Fail to send the email =============> {}", e.getMessage());
            }
            LOGGER.error("Fail to send the email =============> {}", e.getMessage());
        }
    }

}
