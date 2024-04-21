package com.yamo.cdcommoncore.domain.pojo;

import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;

/**
 * @author mowanhui
 * @version 1.0
 * @date 2023/8/28 14:19
 */
public class BaseExcel implements IExcelDataModel, IExcelModel {
    private String errorMsg;

    private Integer rowNum;
    @Override
    public Integer getRowNum() {
        return this.rowNum;
    }

    @Override
    public void setRowNum(Integer i) {
        this.rowNum=i;
    }

    @Override
    public String getErrorMsg() {
        return this.errorMsg;
    }

    @Override
    public void setErrorMsg(String s) {
        this.errorMsg=s;
    }
}
