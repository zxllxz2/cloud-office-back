package com.toby.server.service.impl;

import com.toby.server.mapper.PositionMapper;
import com.toby.server.pojo.Position;
import com.toby.server.service.IPositionService;
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
public class PositionServiceImpl extends ServiceImpl<PositionMapper, Position> implements IPositionService {

}
