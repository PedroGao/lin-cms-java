package com.lin.cms.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lin.cms.demo.vo.PageResult;
import com.lin.cms.demo.bo.GroupPermissionsBO;
import com.lin.cms.demo.common.mybatis.Page;
import com.lin.cms.demo.dto.admin.*;
import com.lin.cms.demo.mapper.GroupPermissionMapper;
import com.lin.cms.demo.model.GroupDO;
import com.lin.cms.demo.model.GroupPermissionDO;
import com.lin.cms.demo.model.UserDO;
import com.lin.cms.demo.model.UserIdentityDO;
import com.lin.cms.demo.service.AdminService;
import com.lin.cms.demo.service.GroupService;
import com.lin.cms.demo.service.UserIdentityService;
import com.lin.cms.demo.service.UserService;
import com.lin.cms.exception.ForbiddenException;
import com.lin.cms.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service("adminServiceImpl-v2")
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserIdentityService userIdentityService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupPermissionMapper groupPermissionMapper;

    @Override
    public PageResult getUserPageByGroupId(Long groupId, Long count, Long page) {
        Page pager = new Page(page, count);
        IPage<UserDO> iPage;
        // 如果group_id为空，则以分页的形式返回所有用户
        if (groupId == null) {
            iPage = userService.page(pager);
        } else {
            iPage = userService.getUserPageByGroupId(pager, groupId);
        }
        return PageResult.genPageResult(iPage.getTotal(), iPage.getRecords(), page, count);
    }

    @Override
    public boolean changeUserPassword(Long id, ResetPasswordDTO dto) {
        throwUserNotExistById(id);
        return userIdentityService.changePassword(id, dto.getNewPassword());
    }

    @Transactional
    @Override
    public boolean deleteUser(Long id) {
        throwUserNotExistById(id);
        boolean userRemoved = userService.removeById(id);
        QueryWrapper<UserIdentityDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserIdentityDO::getUserId, id);
        return userRemoved && userIdentityService.remove(wrapper);
    }

    @Override
    public boolean updateUserInfo(Long id, UpdateUserInfoDTO validator) {
        List<Long> newGroupIds = validator.getGroupIds();
        List<Long> existGroupIds = groupService.getUserGroupIdsByUserId(id);
        // 删除existGroupIds有，而newGroupIds没有的
        List<Long> deleteIds = existGroupIds.stream().filter(it -> !newGroupIds.contains(it)).collect(Collectors.toList());
        // 添加newGroupIds有，而existGroupIds没有的
        List<Long> addIds = newGroupIds.stream().filter(it -> !existGroupIds.contains(it)).collect(Collectors.toList());
        return groupService.deleteUserGroupRelations(id, deleteIds) && groupService.addUserGroupRelations(id, addIds);
    }

    @Override
    public PageResult getGroupPage(Long page, Long count) {
        IPage<GroupDO> iPage = groupService.getGroupPage(page, count);
        return PageResult.genPageResult(iPage.getTotal(), iPage.getRecords(), page, count);
    }

    @Override
    public GroupPermissionsBO getGroup(Long id) {
        throwGroupNotExistById(id);
        return groupService.getGroupAndPermissions(id);
    }

    @Transactional
    @Override
    public boolean createGroup(NewGroupDTO dto) {
        throwGroupNameExist(dto.getName());
        GroupDO group = GroupDO.builder().name(dto.getName()).info(dto.getInfo()).build();
        groupService.save(group);
        List<GroupPermissionDO> relations = dto.getPermissionIds().stream()
                .map(id -> new GroupPermissionDO(group.getId(), id))
                .collect(Collectors.toList());
        return groupPermissionMapper.insertBatch(relations) > 0;
    }

    @Override
    public boolean updateGroup(Long id, UpdateGroupDTO dto) {
        throwGroupNotExistById(id);
        throwGroupNameExist(dto.getName());
        GroupDO group = GroupDO.builder().id(id).name(dto.getName()).info(dto.getInfo()).build();
        return groupService.updateById(group);
    }

    @Override
    public boolean deleteGroup(Long id) {
        throwGroupNotExistById(id);
        return groupService.removeById(id);
    }

    @Override
    public boolean dispatchPermission(DispatchPermissionDTO dto) {
        GroupPermissionDO groupPermission = new GroupPermissionDO(dto.getGroupId(), dto.getPermissionId());
        return groupPermissionMapper.insert(groupPermission) > 0;
    }

    @Override
    public boolean dispatchPermissions(DispatchPermissionsDTO dto) {
        List<GroupPermissionDO> relations = dto.getPermissionIds().stream()
                .map(id -> new GroupPermissionDO(dto.getGroupId(), id))
                .collect(Collectors.toList());
        return groupPermissionMapper.insertBatch(relations) > 0;
    }

    @Override
    public boolean removePermissions(RemovePermissionsDTO dto) {
        return groupPermissionMapper.deleteBatchByGroupIdAndPermissionId(dto.getGroupId(), dto.getPermissionIds()) > 0;
    }

    @Override
    public List<GroupDO> getAllGroups() {
        return groupService.list();
    }

    private void throwUserNotExistById(Long id) {
        boolean exist = userService.checkUserExistById(id);
        if (!exist) {
            throw new NotFoundException("未找到用户");
        }
    }

    private void throwGroupNotExistById(Long id) {
        boolean exist = groupService.checkGroupExistById(id);
        if (!exist) {
            throw new NotFoundException("未找到分组");
        }
    }

    private void throwGroupNameExist(String name) {
        boolean exist = groupService.checkGroupExistByName(name);
        if (exist) {
            throw new ForbiddenException("分组名已被使用，请重新填入新的分组名");
        }
    }
}
