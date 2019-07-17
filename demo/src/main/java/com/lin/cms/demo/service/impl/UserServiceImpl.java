package com.lin.cms.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lin.cms.exception.Forbidden;
import com.lin.cms.exception.Parameter;
import com.lin.cms.demo.mapper.AuthMapper;
import com.lin.cms.demo.mapper.UserMapper;
import com.lin.cms.demo.model.SimpleAuthDO;
import com.lin.cms.demo.model.UserDO;
import com.lin.cms.demo.service.UserService;
import com.lin.cms.demo.utils.AuthSpliter;
import com.lin.cms.demo.utils.LocalUser;
import com.lin.cms.demo.dto.user.RegisterDTO;
import com.lin.cms.demo.dto.user.UpdateInfoDTO;
import com.lin.cms.demo.dto.user.AvatarUpdateDTO;
import com.lin.cms.demo.dto.user.ChangePasswordDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * Created by lin on 2019/06/06.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthMapper authMapper;


    @Override
    public void createUser(RegisterDTO validator) throws Forbidden {
        UserDO exist = this.findByNickname(validator.getNickname());
        // UserDO exist = this.findBy("nickname", validator.getNickname());
        if (exist != null) {
            throw new Forbidden("用户已经存在");
        }
        UserDO user = new UserDO();
        user.setNickname(validator.getNickname());
        user.setPasswordEncrypt(validator.getPassword());
        user.setGroupId(validator.getGroupId());
        user.setNickname(validator.getNickname());
        if (validator.getEmail() != null) {
            user.setEmail(validator.getEmail());
        }
        userMapper.insert(user);
        // this.saveWithTimeCreate(user);
    }

    @Override
    public void updateUser(UpdateInfoDTO validator) throws Parameter {
        String email = validator.getEmail();
        UserDO user = LocalUser.getLocalUser();
        if (!user.getEmail().equals(email)) {
            QueryWrapper<UserDO> wrapper = new QueryWrapper<>();
            wrapper.eq("email", validator.getEmail());
            UserDO exist = userMapper.selectOne(wrapper);
            // UserDO exist = this.findBy("email", validator.getEmail());
            if (exist != null) {
                throw new Parameter("邮箱已被注册，请重新输入邮箱");
            }
        }
        user.setEmail(email);
        // this.update(user);
        userMapper.updateById(user);
    }

    @Override
    public void changePassword(ChangePasswordDTO validator) throws Parameter {
        UserDO user = LocalUser.getLocalUser();
        boolean valid = user.verify(validator.getOldPassword());
        if (!valid) {
            throw new Parameter("请输入正确的旧密码");
        }
        user.setPasswordEncrypt(validator.getNewPassword());
        // this.update(user);
        userMapper.updateById(user);
    }

    @Override
    public void updateAvatar(AvatarUpdateDTO validator) {
        UserDO user = LocalUser.getLocalUser();
        user.setAvatar(validator.getAvatar());
        //this.update(user);
        userMapper.updateById(user);
    }

    @Override
    public List<Map<String, List<Map<String, String>>>> getAuths(Integer groupId) {
        List<SimpleAuthDO> auths = authMapper.findByGroupId(groupId);
        return AuthSpliter.splitAuths(auths);
    }

    @Override
    public UserDO findByNickname(String nickname) {
        QueryWrapper<UserDO> wrapper = new QueryWrapper<>();
        wrapper.eq("nickname", nickname);
        UserDO exist = userMapper.selectOne(wrapper);
        return exist;
    }
}
