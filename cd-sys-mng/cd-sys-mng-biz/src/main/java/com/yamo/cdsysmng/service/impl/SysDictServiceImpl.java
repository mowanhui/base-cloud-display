package com.yamo.cdsysmng.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yamo.cdcommonmybatis.domain.dto.PageDTO;
import com.yamo.cdcommonmybatis.domain.vo.PageVO;
import com.yamo.cdcommoncore.exception.BizException;
import com.yamo.cdcommoncore.utils.*;
import com.yamo.cdcommonmybatis.utils.EntityUtils;
import com.yamo.cdcommonmybatis.utils.PageUtils;
import com.yamo.cdcommonredis.redisObject.DictRedis;
import com.yamo.cdsysmng.domain.dto.DictDTO;
import com.yamo.cdsysmng.domain.entity.SysDict;
import com.yamo.cdsysmng.domain.vo.DictVO;
import com.yamo.cdsysmng.mapper.SysDictMapper;
import com.yamo.cdsysmng.service.ISysDictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 字典表 服务实现类
 * </p>
 *
 * @author mwh
 * @since 2023-08-14
 */
@RequiredArgsConstructor
@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements ISysDictService {
    private final DictRedis dictRedis;
    @Transactional
    @Override
    public void addDictList(DictDTO.AddDictList addDictList) {
        LambdaQueryWrapper<SysDict> pWrapper = new LambdaQueryWrapper<>();
        pWrapper.eq(SysDict::getDictId, addDictList.getParentId())
                .eq(SysDict::getIsDeleted, "0");
        SysDict parentDict = this.getOne(pWrapper);
        if (parentDict == null) {
            throw new BizException("父节点不存在");
        }
        int i = 1;
        for (String name : addDictList.getDictNameList()) {
            DictDTO.AddDict addDict = new DictDTO.AddDict();
            addDict.setParentId(addDictList.getParentId());
            addDict.setDictCode(String.valueOf(i));
            addDict.setDictName(name);
            addDict.setRemark(name);
            addDict.setSort(i);
            addDict.setIsEnable("1");
            addDict(addDict);
            i++;
        }
        dictRedis.delGroup();
    }

    /**
     * 判断字典代码是否存在
     * @param dictCode
     * @param pid
     * @return
     */
    private void checkDictCode(String dictCode,String pid){
        LambdaQueryWrapper<SysDict> wrapper0 = new LambdaQueryWrapper<>();
        wrapper0.eq(SysDict::getDictCode, dictCode)
                .eq(SysDict::getIsDeleted, "0");
        if(StrUtil.isNotBlank(pid)){
            wrapper0.eq(SysDict::getParentId,pid);
        }else {
            wrapper0.isNull(SysDict::getParentId);
        }
        if(this.count(wrapper0)>0){
            throw new BizException(dictCode+"字典代码已存在");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addDict(DictDTO.AddDict addDict) {
        //this.existByName("dict_code",addDict.getDictCode(),addDict.getDictCode()+"字典代码已存在");
        checkDictCode(addDict.getDictCode(),addDict.getParentId());
        String dictTypeCode = null;
        String dictTypeName = null;
        if (StrUtil.isNotBlank(addDict.getParentId())) {
            LambdaQueryWrapper<SysDict> pWrapper = new LambdaQueryWrapper<>();
            pWrapper.eq(SysDict::getDictId, addDict.getParentId())
                    .eq(SysDict::getIsDeleted, "0");
            SysDict parentDict = this.getOne(pWrapper);
            if (parentDict == null) {
                throw new BizException("父节点不存在");
            }
            dictTypeName = parentDict.getDictName();
            if (StrUtil.isBlank(parentDict.getDictTypeCode())) {
                dictTypeCode = "-" + parentDict.getDictCode() + "-";
            } else {
                dictTypeCode = parentDict.getDictTypeCode() + parentDict.getDictCode() + "-";
            }
        }
        String userId = RequestUtils.getUserId();
        SysDict sysDict = new SysDict();
        BeanUtil.copyProperties(addDict, sysDict);
        String dictId = UUIDUtils.getUUID();
        sysDict.setDictId(dictId);
        sysDict.setDictTypeCode(dictTypeCode);
        sysDict.setDictTypeName(dictTypeName);
        EntityUtils.setCreateProperties(sysDict, userId);
        if (StrUtil.isBlank(sysDict.getParentId())) {
            sysDict.setParentId(null);
        }
//        sort(sysDict, SortTypeEnum.ADD, true);
        this.save(sysDict);
        dictRedis.delGroup();
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void editDict(DictDTO.EditDict editDict) {
        SysDict sysDict = this.getByName("dict_id", editDict.getDictId());
        int sourceSort=sysDict.getSort();
        if (!editDict.getDictCode().equals(sysDict.getDictCode())) {
            checkDictCode(editDict.getDictCode(),editDict.getParentId());
            //修改typeCode
            LambdaQueryWrapper<SysDict> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysDict::getIsDeleted, "0")
                    .like(SysDict::getDictTypeCode, "-" + sysDict.getDictCode() + "-");
            List<SysDict> dictList = this.list(wrapper);
            if (dictList != null && dictList.size() > 0) {
                dictList.forEach(item -> {
                    item.setDictTypeCode(item.getDictTypeCode().replace("-" + sysDict.getDictCode() + "-", "-" + editDict.getDictCode() + "-"));
                });
                this.updateBatchById(dictList);
            }
        }
        BeanUtil.copyProperties(editDict, sysDict);
        String userId = RequestUtils.getUserId();
                EntityUtils.setUpdateProperties(sysDict, userId);
//        if(editDict.getSort()!=sourceSort){
//            sort(sysDict, SortTypeEnum.EDIT, true);
//        }
        this.updateById(sysDict);
        dictRedis.delGroup();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delDict(DictDTO.DelDict delDict) {
        SysDict sysDict = this.getByName("dict_id", delDict.getDictId());
//        sort(sysDict, SortTypeEnum.DELETE, true);
        String userId = RequestUtils.getUserId();
        //删除
        this.lambdaUpdate()
                .eq(SysDict::getIsDeleted, "0")
                .and(i -> {
                    i.like(SysDict::getDictTypeCode, "-" + sysDict.getDictCode() + "-")
                            .or()
                            .eq(SysDict::getDictCode, sysDict.getDictCode());
                })
                .set(SysDict::getIsDeleted, "1")
                .set(SysDict::getDeleteBy, userId)
                .set(SysDict::getDeleteTime, new Date())
                .update();
        dictRedis.delGroup();
    }

    @Override
    public List<DictVO> getDictTree(DictDTO.SearchDict searchDict) {
        List<DictVO> dictVOList=null;
        if(StrUtil.isNotBlank(searchDict.getDictTypeCode())&&dictRedis.hasKey(searchDict.getDictTypeCode())){
            JSONArray array=dictRedis.get(searchDict.getDictTypeCode());
            dictVOList=array.toList(DictVO.class);
        }else {
            dictVOList = this.baseMapper.getDictList(searchDict);
        }
        dictVOList.forEach(s -> {
            s.setTreeId(s.getDictId());
            s.setTreeParentId(s.getParentId());
            s.setTreeSort(s.getSort().longValue());
        });
        return TreeUtils.toTree(dictVOList);
    }

    @Override
    public List<DictVO> getDictList(DictDTO.SearchDict searchDict) {
        List<DictVO> dictVOList=null;
        if(StrUtil.isNotBlank(searchDict.getDictTypeCode())&&dictRedis.hasKey(searchDict.getDictTypeCode())){
            JSONArray array=dictRedis.get(searchDict.getDictTypeCode());
            dictVOList=array.toList(DictVO.class);
        }else {
            dictVOList = this.baseMapper.getDictList(searchDict);
        }
        return dictVOList;
    }

    @Override
    public PageVO<DictVO> getDictPage(DictDTO.SearchDictPage searchDictPage) {
        DictDTO.SearchDict searchDict=BeanUtil.toBean(searchDictPage,DictDTO.SearchDict.class);
        PageDTO pageDTO=searchDictPage;
        return PageUtils.doPage(pageDTO,()->this.baseMapper.getDictList(searchDict));
    }
}
