package com.lin.cms.demo.dto.admin;

import com.lin.cms.demo.validator.NotEmptyFields;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class NewGroupDTO {


    @NotBlank(message = "请输入分组名称")
    private String name;

    private String info;

    @NotEmptyFields(message = "请输入auths字段，且每一项不可为空")
    private List<String> auths;
}
