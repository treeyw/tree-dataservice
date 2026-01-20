package io.github.treeyw.dataservice.controller.demo;

import io.github.treeyw.crud.model.ApiResult;
import io.github.treeyw.crud.model.demo.TreeywDemoDO;
import io.github.treeyw.dataservice.controller.common.ParentController;
import io.github.treeyw.dataservice.model.common.ConsoleLog;
import io.github.treeyw.dataservice.model.common.ConsoleParam;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = {"/apiDemo/"})
@ConsoleLog(name = "基础api", tags = "Demo模块")
public class DemoController extends ParentController {

    //查询参数里有值则自动按值查询，例如 name=xx，page=1 pageSize=20  sort = id 等
    @RequestMapping("list")
    @ConsoleLog(name = "查询TreeywDemoDO", tags = "Demo模块",
            enterParamModel = TreeywDemoDO.class,
            notes = "根据传入参数动态查询列表，支持多种查询方式，详见公共参数说明")
    public ApiResult list(TreeywDemoDO to) throws Exception {
        return ApiResult.ok(parentQuery.listQuery(to));
    }

    @Transactional
    @RequestMapping("save")
    @ConsoleLog(name = "新增或修改TreeywDemoDO", tags = "Demo模块",
            enterParamModel = TreeywDemoDO.class,
            notes = "根据传入的TreeywDemoDO对象进行新增或修改操作，若对象中包含id则为修改，否则为新增")
    public ApiResult save(TreeywDemoDO to) throws Exception {

        return ApiResult.ok(parentModify.save(to));
    }

    @RequestMapping("del")
    @ConsoleLog(name = "删除TreeywDemoDO", tags = "Demo模块",
            enterParameters = {
                    @ConsoleParam(name = "id", explain = "主键ID", example = "1", type = Long.class)
            },
            notes = "根据传入的主键ID删除对应的TreeywDemoDO记录，执行物理删除操作")
    public ApiResult del(long id) throws Exception {
        //物理删除为parentModify.sysDeleteById(to.getId());
        TreeywDemoDO to = new TreeywDemoDO();
        to.setId(id);
        return ApiResult.ok(parentModify.parentDelete(to));
    }


}
