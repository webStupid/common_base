package com.wwb.commonbase.mybatisplus;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wwb.commonbase.utils.entities.PageEntity;
import com.wwb.commonbase.utils.response.ResponsePageResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.function.Function;

/**
 * 分页搜索条件实体类基类
 *
 * @author xxx
 */
@Data
@ApiModel(value = "IPageSearchModel", description = "分页搜索条件实体类基类")
public abstract class IPageSearchModel<T> extends ISearchModel<T> {
    @ApiModelProperty("分页条件")
    private PageEntity page;

    public Page<T> toPage() {
        PageEntity pageEntity = getPage();
        if (pageEntity != null) {
            return new Page<>(pageEntity.getPageindex(), pageEntity.getPagesize());
        }
        return null;
    }

    public ResponsePageResult<T> queryPage(IService<T> service, Function<T, T> converter) {
        if (getPage() != null) {
            IPage<T> res = service.page(toPage(), toQueryWrapper());
            return ResponsePageResult.success(res.getTotal(), entityMap(res.getRecords(), converter));
        } else {
            List<T> res = service.list(toQueryWrapper());
            long total = res != null ? res.size() : 0;
            return ResponsePageResult.success(total, entityMap(res, converter));
        }
    }

    public ResponsePageResult<T> queryPage(IService<T> service) {
        return queryPage(service, null);
    }
}
