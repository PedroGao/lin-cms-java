package com.lin.cms.demo.common.interceptor;

import com.lin.cms.demo.common.LocalUser;
import com.lin.cms.demo.model.UserDO;
import com.lin.cms.interfaces.LoggerResolver;
import com.lin.cms.core.annotation.Logger;
import com.lin.cms.core.annotation.RouteMeta;
import com.lin.cms.core.utils.BeanUtil;
import com.lin.cms.demo.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class LoggerImpl implements LoggerResolver {

    @Autowired
    private LogService logService;

    public static String REG_XP = "(?<=\\{)[^}]*(?=\\})";

    private Pattern pattern = Pattern.compile(REG_XP);


    @Override
    public void handle(RouteMeta meta, Logger logger, HttpServletRequest request, HttpServletResponse response) {
        // parse template and extract properties from request,response and modelAndView
        String template = logger.template();
        UserDO user = LocalUser.getLocalUser();
        template = this.parseTemplate(template, user, request, response);
        String permission = meta.permission();
        Long userId = user.getId();
        String userName = user.getNickname();
        String method = request.getMethod();
        String path = request.getServletPath();
        Integer status = response.getStatus();
        logService.createLog(template, permission, userId, userName, method, path, status);
    }

    private String parseTemplate(String template, UserDO user, HttpServletRequest request, HttpServletResponse response) {
        // 调用 get 方法
        Matcher m = pattern.matcher(template);
        while (m.find()) {
            String group = m.group();
            String property = this.extractProperty(group, user, request, response);
            template = template.replace("{" + group + "}", property);
        }
        return template;
    }

    private String extractProperty(String item, UserDO user, HttpServletRequest request, HttpServletResponse response) {
        int i = item.lastIndexOf('.');
        String obj = item.substring(0, i);
        String prop = item.substring(i + 1);
        switch (obj) {
            case "user":
                if (user == null) {
                    return "";
                }
                return BeanUtil.getValueByPropName(user, prop);
            case "request":
                return BeanUtil.getValueByPropName(request, prop);
            case "response":
                return BeanUtil.getValueByPropName(response, prop);
            default:
                return "";
        }
    }
}
