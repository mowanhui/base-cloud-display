package com.yamo.cdcommoncore.utils;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yamo.cdcommoncore.exception.BizException;
import com.yamo.cdcommoncore.result.ResultCode;
import com.yamo.cdcommoncore.support.CFunction;
import com.yamo.cdcommoncore.support.DictHandler;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 字典工具类
 *
 * @author mowanhui
 * @version 1.0
 * @date 2023/8/23 15:09
 */
public class DictUtils<T> {
    private List<T> dataList;
    private List<DictConvert> dictConvertList;

    private List<DictConvertOfJson> dictConvertListOfJson;
    /**
     * 字典处理
     */
    private final DictHandler dictHandler;

    public DictUtils(DictHandler dictHandler) {
        this.dictHandler = dictHandler;
    }

    /**
     * 设置需要字典值转化的列表
     *
     * @param dataList
     * @return
     */
    public DictUtils<T> setDataList(List<T> dataList) {
        this.dataList = dataList;
        return this;
    }

    /**
     * 设置需要字典值转化的对象
     *
     * @param data
     * @return
     */
    public DictUtils<T> setData(T data) {
        List<T> dataList=new ArrayList<>();
        dataList.add(data);
        this.dataList = dataList;
        return this;
    }

    /**
     * 构建字典转化类型的列表
     *
     * @return
     */
    public DictConvert buildDictConvert() {
        if(dictConvertList==null){
            dictConvertList = new ArrayList<>();
        }
        DictConvert dictConvert = new DictConvert();
        dictConvertList.add(dictConvert);
        return dictConvert;
    }

    /**
     * 构建字典转化类型的列表,json格式
     *
     * @return
     */
    public DictConvertOfJson buildDictConvertOfJson() {
        if(dictConvertListOfJson==null){
            dictConvertListOfJson = new ArrayList<>();
        }
        DictConvertOfJson dictConvert = new DictConvertOfJson();
        dictConvertListOfJson.add(dictConvert);
        return dictConvert;
    }


    /**
     * 字典转换完成
     */
    private void finish(ConvertType convertType) {
        if (dictConvertList.size() == 0 || dataList.size() == 0) {
            return;
        }
        try {
            List<DictDealInfo> dictDealInfoList = new ArrayList<>();
            for (DictConvert dictConvert : dictConvertList) {
                DictDealInfo dictDealInfo = new DictDealInfo();
                dictDealInfo.setDictType(dictConvert.getDictType());
                if (dictConvert.getSource() == null) {
                    throw new BizException("source不能数为空");
                }
                JSONArray dictList = getDictList(dictHandler, dictConvert.getDictType());
                if (dictList.size() == 0) {
                    continue;
                }
                String sourceName = fieldName(dictConvert.getSource());
                String targetName = dictConvert.getTarget() == null
                        || dictConvert.getSource() == dictConvert.getTarget()
                        ? sourceName : fieldName(dictConvert.getTarget());
                String updateName = targetName.replace("get", "set");
                dictDealInfo.setSourceName(sourceName);
                dictDealInfo.setTargetName(targetName);
                dictDealInfo.setDictList(dictList);
                dictDealInfo.setUpdateName(updateName);
                dictDealInfoList.add(dictDealInfo);
            }

            for (T t : dataList) {
                for (DictDealInfo dictDealInfo : dictDealInfoList) {
                    Class<?> tc = t.getClass();
                    Method m = tc.getMethod(dictDealInfo.getSourceName());
                    String val1 = (String) m.invoke(t);
                    if (StringUtils.isBlank(val1)) {
                        continue;
                    }
                    for (Object ob : dictDealInfo.getDictList()) {
                        JSONObject o = JSONUtil.parseObj(ob);
                        String val2 = o.getStr("dictCode");
                        String val3 = o.getStr("dictName");
                        if (ConvertType.TO_CODE == convertType) {
                            val2 = o.getStr("dictName");
                            val3 = o.getStr("dictCode");
                        }
                        if (val1.equals(val2)) {
                            try {
                                Method m1 = tc.getMethod(dictDealInfo.getUpdateName(), String.class);
                                m1.invoke(t, val3);
                                break;
                            } catch (Exception e) {
                                e.printStackTrace();
                                throw new BizException(ResultCode.BUSY);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(e.getMessage());
        }
    }


    /**
     * 字典转换完成
     */
    private void finishOfJson(ConvertType convertType) {
        if (dictConvertListOfJson.size() == 0 || dataList.size() == 0) {
            return;
        }
        List<DictDealInfo> dictDealInfoList = new ArrayList<>();
        for (DictConvertOfJson dictConvert : dictConvertListOfJson) {
            DictDealInfo dictDealInfo = new DictDealInfo();
            dictDealInfo.setDictType(dictConvert.getDictType());
            if (dictConvert.getSource() == null) {
                throw new BizException("source不能数为空");
            }
            JSONArray dictList = getDictList(dictHandler, dictConvert.getDictType());
            if (dictList.size() == 0) {
                continue;
            }
            String sourceName = dictConvert.getSource();
            String targetName = dictConvert.getTarget() == null
                    || Objects.equals(dictConvert.getSource(), dictConvert.getTarget())
                    ? sourceName : dictConvert.getTarget();
            dictDealInfo.setSourceName(sourceName);
            dictDealInfo.setTargetName(targetName);
            dictDealInfo.setDictList(dictList);
            dictDealInfoList.add(dictDealInfo);
        }

        for (T t : dataList) {
            for (DictDealInfo dictDealInfo : dictDealInfoList) {
                JSONObject jt = (JSONObject) t;
                String val1 = jt.getStr(dictDealInfo.getSourceName());
                if (StringUtils.isBlank(val1)) {
                    continue;
                }
                for (Object ob : dictDealInfo.getDictList()) {
                    JSONObject o = JSONUtil.parseObj(ob);
                    String val2 = o.getStr("dictCode");
                    String val3 = o.getStr("dictName");
                    if (ConvertType.TO_CODE == convertType) {
                        val2 = o.getStr("dictName");
                        val3 = o.getStr("dictCode");
                    }
                    if (val1.equals(val2)) {
                        jt.set(dictDealInfo.getTargetName(), val3);
                        break;
                    }
                }
            }
        }

    }


    private JSONArray getDictList(DictHandler dictHandler, String dictType) {
        return dictHandler.getDictList(dictType);
    }

    private String fieldName(CFunction<T, ?> f) {
        try {
            Class<?> r = f.getClass();
            Method method = r.getDeclaredMethod("writeReplace");
            method.setAccessible(true);
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(f);
            return serializedLambda.getImplMethodName();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(ResultCode.BUSY);
        }
    }

    /**
     * 字典转化信息类
     */
    @Getter
    public class DictConvert {
        /**
         * 字典类型
         */
        private String dictType;
        /**
         * 转化前的字段
         */
        private CFunction<T, String> source;
        /**
         * 转化后的字段
         */
        private CFunction<T, String> target;

        public DictConvert setDictType(String dictType) {
            this.dictType = dictType;
            return this;
        }

        public DictConvert setSource(CFunction<T, String> source) {
            this.source = source;
            return this;
        }

        public DictConvert setTarget(CFunction<T, String> target) {
            this.target = target;
            return this;
        }

        public DictConvert next() {
            return buildDictConvert();
        }

        /**
         * 转换为code
         */
        public void toCode() {
            finish(ConvertType.TO_CODE);
        }

        /**
         * 转换为name
         */
        public void toName() {
            finish(ConvertType.TO_NAME);
        }
    }


    /**
     * 字典转化信息类
     */
    @Getter
    public class DictConvertOfJson {
        /**
         * 字典类型
         */
        private String dictType;
        /**
         * 转化前的字段
         */
        private String source;
        /**
         * 转化后的字段
         */
        private String target;

        public DictConvertOfJson setDictType(String dictType) {
            this.dictType = dictType;
            return this;
        }

        public DictConvertOfJson setSource(String source) {
            this.source = source;
            return this;
        }

        public DictConvertOfJson setTarget(String target) {
            this.target = target;
            return this;
        }

        public DictConvertOfJson next() {
            return buildDictConvertOfJson();
        }

        /**
         * 转换为code
         */
        public void toCode() {
            finishOfJson(ConvertType.TO_CODE);
        }

        /**
         * 转换为name
         */
        public void toName() {
            finishOfJson(ConvertType.TO_NAME);
        }
    }

    /**
     * 字典处理信息
     */
    @Data
    class DictDealInfo {
        private String dictType;
        private String sourceName;
        private String targetName;
        private String updateName;
        private JSONArray dictList;
    }

    enum ConvertType {
        TO_CODE,
        TO_NAME
    }
}
