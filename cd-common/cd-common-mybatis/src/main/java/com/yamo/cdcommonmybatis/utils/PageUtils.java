package com.yamo.cdcommonmybatis.utils;

import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageSerializable;
import com.yamo.cdcommonmybatis.domain.dto.PageDTO;
import com.yamo.cdcommonmybatis.domain.vo.PageVO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义分页
 */
@Data
public class PageUtils {

    private PageUtils() {
    }


    /**
     * 默认使用分页
     * 使用pagehelper进行分页，该分页只能一对一
     */
    public static <T> PageVO<T> doPage(PageDTO pageDTO, ISelect select) {
        PageSerializable<T> simplePageInfo = PageHelper.startPage(pageDTO).doSelectPageSerializable(select);
        PageVO<T> pageVO = new PageVO<>();
        pageVO.setPageNum(pageDTO.getPageNum());
        pageVO.setPageSize(pageDTO.getPageSize());
        pageVO.setList(simplePageInfo.getList());
        pageVO.setTotal(simplePageInfo.getTotal());
        pageVO.setPages(getPages(simplePageInfo.getTotal(), pageDTO.getPageSize()));
        return pageVO;
    }


    /**
     * 获取页数
     * @param total
     * @param pageSize
     * @return
     */
    public static Integer getPages(long total, Integer pageSize) {

        if (total == -1) {
            return 1;
        }
        if (pageSize > 0) {
            return  (int) (total / pageSize + ((total % pageSize == 0) ? 0 : 1));
        }
        return  0;
    }

    /**
     * 对全列表进行分页
     * @param total 总记录
     * @param list 所有数据
     * @param pageDTO
     * @return
     * @param <T>
     */
    public static <T> PageVO<T> pageInfo(int total,List<T> list, PageDTO pageDTO) {
        PageVO<T> page = new PageVO<>();
        if (list == null || list.size() == 0 || total<=0) {
            page.setPageSize(pageDTO.getPageSize());
            page.setPageNum(pageDTO.getPageNum());
            page.setTotal(0L);
            page.setPages(0);
            return page;
        }
        //总页数
        int totalPage = total / pageDTO.getPageSize();
        if (total % pageDTO.getPageSize() > 0) {
            totalPage += 1;
        }
        page.setPageSize(pageDTO.getPageSize());
        page.setPageNum(pageDTO.getPageNum());
        page.setPages(totalPage);
        page.setTotal((long) total);
        int startIndex=(pageDTO.getPageNum()-1)*pageDTO.getPageSize();
        int endIndex=0;
        if(startIndex>total){
            page.setList(new ArrayList<>());
        }else {
           if(pageDTO.getPageNum()*pageDTO.getPageSize()>total){
               endIndex=total;
           }else {
               endIndex=pageDTO.getPageNum()*pageDTO.getPageSize();
           }
        }
        page.setList(list.subList(startIndex,endIndex));
        return page;
    }
}
