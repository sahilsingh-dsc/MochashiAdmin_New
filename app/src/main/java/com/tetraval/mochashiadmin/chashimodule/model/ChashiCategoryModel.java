package com.tetraval.mochashiadmin.chashimodule.model;

public class ChashiCategoryModel {
    String c_uid;
    String c_name;
    String c_image;

    public ChashiCategoryModel(String c_uid, String c_name, String c_image) {
        this.c_uid = c_uid;
        this.c_name = c_name;
        this.c_image = c_image;
    }

    public ChashiCategoryModel() {
    }

    public String getC_uid() {
        return c_uid;
    }

    public String getC_name() {
        return c_name;
    }

    public String getC_image() {
        return c_image;
    }

}
