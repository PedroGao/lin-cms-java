package com.lin.cms.demo.sleeve.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author pedro
 * @since 2019-07-23
 */
public class Spu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String title;

    private String subtitle;

    private Long categoryId;

    private Integer onSale;

    @JSONField(serialize = false)
    private Date createTime;

    @JSONField(serialize = false)
    private Date updateTime;

    @JSONField(serialize = false)
    @TableLogic
    private Date deleteTime;

    private Long brandId;

    /**
     * 文本型价格，有时候SPU需要展示的是一个范围，或者自定义平均价格
     */
    private String price;

    /**
     * 某种规格可以直接附加单品图片
     */
    private Long sketchSpecId;

    /**
     * 默认选中的sku
     */
    private Long defaultSkuId;

    private String img;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getOnSale() {
        return onSale;
    }

    public void setOnSale(Integer onSale) {
        this.onSale = onSale;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Long getSketchSpecId() {
        return sketchSpecId;
    }

    public void setSketchSpecId(Long sketchSpecId) {
        this.sketchSpecId = sketchSpecId;
    }

    public Long getDefaultSkuId() {
        return defaultSkuId;
    }

    public void setDefaultSkuId(Long defaultSkuId) {
        this.defaultSkuId = defaultSkuId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "Spu{" +
                "id=" + id +
                ", title=" + title +
                ", subtitle=" + subtitle +
                ", categoryId=" + categoryId +
                ", onSale=" + onSale +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", deleteTime=" + deleteTime +
                ", brandId=" + brandId +
                ", price=" + price +
                ", sketchSpecId=" + sketchSpecId +
                ", defaultSkuId=" + defaultSkuId +
                ", img=" + img +
                "}";
    }
}