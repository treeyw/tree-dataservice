package io.github.treeyw.dataservice.controller.common;


import io.github.treeyw.crud.model.ApiResult;
import io.github.treeyw.crud.model.parent.ParentDO;
import io.github.treeyw.crud.service.common.ParentSevice;
import io.github.treeyw.dataservice.model.common.ConsoleLog;
import io.github.treeyw.dataservice.model.common.ConsoleLogBO;
import io.github.treeyw.dataservice.model.common.ConsoleParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.github.treeyw.dataservice.init.BlockAttribute.DOC_TAGS_MAP;
import static io.github.treeyw.dataservice.init.BlockAttribute.DOC_URL_MAP;


@RestController
@RequestMapping(value = {"/api/"})
@ConsoleLog(name = "说明", tags = "公共参数")
public class ApiController extends ParentController {

    @RequestMapping(value = "demo")
    @ConsoleLog(name = "公共字段", enterParameters = {
            @ConsoleParam(name = "id", explain = "主键id", example = "1", type = Long.class),
            @ConsoleParam(name = "createTime", explain = "创建时间", example = "2022-02-02 12:12:12", type = Date.class),
            @ConsoleParam(name = "updateTime", explain = "修改时间", example = "2022ParentController-02-02 12:12:12", type = Date.class),
            @ConsoleParam(name = "createUserid", explain = "创建人员", example = "1", type = String.class),
            @ConsoleParam(name = "page", explain = "页码", example = "1", type = Integer.class),
            @ConsoleParam(name = "pageSize", explain = "单页数量", example = "20", type = Integer.class),
            @ConsoleParam(name = "sordcloum", explain = "排序字段", example = "updateTime,id", type = String.class),
            @ConsoleParam(name = "sord", explain = "排序方式", example = "desc", type = String.class),
            @ConsoleParam(name = "deleteFlag", explain = "是否删除", example = "0", type = Integer.class),
            @ConsoleParam(name = "unitcode", explain = "录入单位", example = "110000", type = String.class),
    })
    public ApiResult demo(ParentDO map) throws Exception {
        return ApiResult.ok(map);
    }

    @RequestMapping(value = "queryType")
    @ConsoleLog(name = "多维度查询支持的参数", enterParameters = {
            @ConsoleParam(name = "equals", explain = "等于", example = "id$equals=1"),
            @ConsoleParam(name = "ne", explain = "不等于", example = "id$ne=1"),
            @ConsoleParam(name = "isNull", explain = "为空", example = "id$isNull=1"),
            @ConsoleParam(name = "isNotNull", explain = "不为空", example = "id$isNotNull=1"),
            @ConsoleParam(name = "split_in", explain = "英文逗号分割后in查询", example = "id$split_in=1,2,3"),
            @ConsoleParam(name = "in", explain = "直接in(建议用上一种)", example = "id$in=[1,2]"),
            @ConsoleParam(name = "like", explain = "前后模糊查询", example = "id$like=1"),
            @ConsoleParam(name = "like_befor", explain = "前模糊查询", example = "id$like_befor=1"),
            @ConsoleParam(name = "like_after", explain = "后模糊查询", example = "id$like_after=1"),
            @ConsoleParam(name = "gt", explain = "大于", example = "id$gt=1"),
            @ConsoleParam(name = "lt", explain = "小于", example = "id$lt=1"),
            @ConsoleParam(name = "ge", explain = "大于等于", example = "id$ge=1"),
            @ConsoleParam(name = "le", explain = "小于等于", example = "id$le=1")
    },
            notes = "传参目前支持formData，" +
                    "例如查询字段为name，查询目标是name等于“张三还”有“李四”则是 'name$split_in' = '张三,李四'，" +
                    "\n如果查询目标是姓张的的则是 'name$like_befor' : '张'"
    )
    public ApiResult queryType(ParentDO map) throws Exception {
        return ApiResult.ok(map);
    }

    @RequestMapping(value = "docTagsHaveEntity")
    @ConsoleLog(name = "获取项目所有文档-含实体")
    public ApiResult docTagsHaveEntity() {
        return ApiResult.ok(DOC_TAGS_MAP);
    }

    @RequestMapping(value = "docTags")
    @ConsoleLog(name = "获取项目所有文档")
    public ApiResult docTags() {
        Map<String, Map<String, List<ConsoleLogBO>>> noEntityDOC = new LinkedHashMap<>();
        DOC_TAGS_MAP.forEach((k, v) -> {
            if (!k.contains("实体类-com")) noEntityDOC.put(k, v);
        });
        return ApiResult.ok(noEntityDOC);
    }

    @RequestMapping(value = "docByUrl")
    @ConsoleLog(name = "获取某个文档", enterParameters = {
            @ConsoleParam(name = "url", explain = "url")
    })
    public ApiResult docByUrl(String url) {
        return ApiResult.ok(DOC_URL_MAP.get(url));
    }


}
