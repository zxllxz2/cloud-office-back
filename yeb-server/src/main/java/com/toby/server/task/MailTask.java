package com.toby.server.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.toby.server.pojo.Employee;
import com.toby.server.pojo.MailConstants;
import com.toby.server.pojo.MailLog;
import com.toby.server.service.IEmployeeService;
import com.toby.server.service.IMailLogService;
import org.apache.tomcat.jni.Local;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 邮件发送定时任务
 */
@Component
public class MailTask {

    @Autowired
    private IMailLogService mailLogService;

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 邮件发送定时任务
     * 每10秒执行一次
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void mailTask() {
        List<MailLog> list = mailLogService.list(new QueryWrapper<MailLog>().eq("status", 0)
                .lt("tryTime", LocalDateTime.now()));
        list.forEach(mailLog -> {
            //若重试次数超过三次，则更新状态为”投递失败“，不再重试
            if (mailLog.getCount() >= 3) {
                mailLogService.update(new UpdateWrapper<MailLog>().set("status", 2)
                        .eq("msgId", mailLog.getMsgId()));
            } else {
                mailLogService.update(new UpdateWrapper<MailLog>().set("count", mailLog.getCount() + 1)
                        .set("updateTime", LocalDateTime.now())
                        .set("tryTime", LocalDateTime.now().plusMinutes(MailConstants.MSG_TIMEOUT))
                        .eq("msgId", mailLog.getMsgId()));
                //获取对应员工实体并重新发送消息
                Employee emp = employeeService.getEmployee(mailLog.getEid()).get(0);
                rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME,
                        MailConstants.MAIL_ROUTING_KEY_NAME, emp,
                        new CorrelationData(mailLog.getMsgId()));
            }
        });
    }

}
