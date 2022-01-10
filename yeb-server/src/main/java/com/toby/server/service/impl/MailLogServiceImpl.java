package com.toby.server.service.impl;

import com.toby.server.mapper.MailLogMapper;
import com.toby.server.pojo.MailLog;
import com.toby.server.service.IMailLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tobyzhu
 * @since 2021-12-31
 */
@Service
public class MailLogServiceImpl extends ServiceImpl<MailLogMapper, MailLog> implements IMailLogService {

}
