package com.toby.server.service.impl;

import com.toby.server.mapper.NationMapper;
import com.toby.server.pojo.Nation;
import com.toby.server.service.INationService;
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
public class NationServiceImpl extends ServiceImpl<NationMapper, Nation> implements INationService {

}
