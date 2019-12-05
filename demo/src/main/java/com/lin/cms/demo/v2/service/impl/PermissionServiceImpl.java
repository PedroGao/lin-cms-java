package com.lin.cms.demo.v2.service.impl;

import com.lin.cms.demo.v2.model.PermissionDO;
import com.lin.cms.demo.v2.mapper.PermissionMapper;
import com.lin.cms.demo.v2.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pedro
 * @since 2019-11-30
 */
@Service("permissionServiceImpl-v2")
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, PermissionDO> implements PermissionService {


    @Override
    public List<PermissionDO> getPermissionByGroupIDs(List<Long> groupIDs) {
        return this.baseMapper.selectPermissionsByGroupIDs(groupIDs);
    }

    /**
     * 为什么不使用联表进行查询？
     * 1. 联表很麻烦，需要关联2，3次，涉及到3张表，会严重影响性能
     * 2. 由于使用了IN关键字，所以性能其实很不好
     * 3. 不直观，可读性差
     * 4. 用户的分组一般都比较少，一般情况下都在2个一下
     */
    @Override
    public Map<List<PermissionDO>, Long> getPermissionMapByGroupIDs(List<Long> groupIDs) {
        HashMap map = new HashMap(groupIDs.size());
        groupIDs.stream().forEach(groupID -> {
            List<PermissionDO> permissions = this.baseMapper.selectPermissionsByGroupID(groupID);
            map.put(groupID, permissions);
        });
        return map;
    }

    @Override
    public List<Map<String, List<Map<String, String>>>> structuringPermissions(List<PermissionDO> permissions) {
        Map<String, List<Map<String, String>>> tmp = new HashMap();
        permissions.forEach(permission -> {
            if (!tmp.containsKey(permission.getModule())) {
                Map<String, String> tiny = new HashMap();
                tiny.put("module", permission.getModule());
                tiny.put("permission", permission.getName());
                List<Map<String, String>> mini = new ArrayList();
                mini.add(tiny);
                tmp.put(permission.getModule(), mini);
            } else {
                Map<String, String> tiny = new HashMap();
                tiny.put("module", permission.getModule());
                tiny.put("permission", permission.getName());
                tmp.get(permission.getModule()).add(tiny);
            }
            permission.getName();
        });
        List<Map<String, List<Map<String, String>>>> structualPermissions = new ArrayList();
        tmp.forEach((k, v) -> {
            Map<String, List<Map<String, String>>> ttmp = new HashMap();
            ttmp.put(k, v);
            structualPermissions.add(ttmp);
        });
        return structualPermissions;
    }

    @Override
    public Map<String, List<String>> structuringPermissionsSimply(List<PermissionDO> permissions) {
        // mod      permission.names
        Map<String, List<String>> res = new HashMap<>();
        permissions.forEach(permission -> {
            if (res.containsKey(permission.getModule())) {
                List<String> mod = res.get(permission.getModule());
                mod.add(permission.getName());
            } else {
                List<String> mod = new ArrayList<>();
                mod.add(permission.getName());
                res.put(permission.getModule(), mod);
            }
        });
        return res;
    }
}
