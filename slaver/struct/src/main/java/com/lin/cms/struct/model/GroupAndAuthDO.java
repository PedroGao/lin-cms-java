package com.lin.cms.struct.model;

import lombok.Data;

@Data
public class GroupAndAuthDO {
    private Integer id;

    private String name;

    private String info;

    private String auth;

    private String module;
}