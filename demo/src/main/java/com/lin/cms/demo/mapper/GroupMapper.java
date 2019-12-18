package com.lin.cms.demo.mapper;

import com.lin.cms.demo.model.GroupDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author pedro
 * @since 2019-11-30
 */
public interface GroupMapper extends BaseMapper<GroupDO> {

    /**
     * 获得用户的所有分组
     *
     * @param userId 用户id
     * @return 所有分组
     */
    List<GroupDO> selectUserGroups(@Param("userId") Long userId);

    /**
     * 获得用户的所有分组id
     *
     * @param userId 用户id
     * @return 所有分组id
     */
    List<Long> selectUserGroupIds(@Param("userId") Long userId);

    /**
     * 通过id获得分组个数
     *
     * @param id id
     * @return 个数
     */
    int selectCountById(@Param("id") Long id);

    /**
     * 检查用户是否在该名称的分组里
     *
     * @param userId    用户id
     * @param groupName 分组名
     */
    int selectCountUserByUserIdAndGroupName(@Param("userId") Long userId, @Param("groupName") String groupName);
}
