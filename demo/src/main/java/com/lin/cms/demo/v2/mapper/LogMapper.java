package com.lin.cms.demo.v2.mapper;

import com.lin.cms.demo.v2.model.LogDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Service;

/**
 * @author pedro
 * @since 2019-11-30
 */
@Service("logMapper2")
public interface LogMapper extends BaseMapper<LogDO> {

}
