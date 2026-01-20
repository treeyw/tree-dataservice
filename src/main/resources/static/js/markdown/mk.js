$(function () {
    getData();
});


var pageNum = 1;

function search() {
    pageNum = 1;
    getData();
}

function getData() {
    var classname = getUrlParam("optClass"), id = getUrlParam("id"), paramJson = getUrlParam("paramJson"),
        cloumList = getUrlParam("cloumList");
    var listTinputHtml = "<div class=\"input-group\" style=\"width: 24.5%;margin-left:0.25%;margin-right:0.25%;float: left;margin-top: 5px;\">\n" +
        "        <span class=\"input-group-addon\">$ch$</span>\n" +
        "        <input id='tinp_$en$' clo='' type=\"text\" class=\"form-control yhInp read\">\n" +
        "    </div>";
    //准备参数
    var jsonMap = {};
    if (paramJson != null) $.each(JSON.parse(paramJson), function (key, values) {
        jsonMap[key] = values;
    });
    //输入框渲染
    var inpList = $(".yhInp");
    for (var i = 0; i < inpList.length; i++) {
        var tinp = inpList[i].id;
        if ($("#" + tinp).val().length == 0) continue;
        jsonMap [tinp.replaceAll("tinp_", "")] = $("#" + tinp).val();
    }
    //初始化页码
    jsonMap["page"] = pageNum;
    jsonMap["pageSize"] = 10;
    //获取返回值
    var cloums = [];
    var list = [];
    $.ajax({
        type: "post",
        url: "/sys-console/begin/skip/list/common_begin_BeginMarkdown",
        data: {
            token: getUrlParam("token"),
            paramJson: JSON.stringify(jsonMap),
            cloumList: cloumList
        },
        success(msg) {
            cloums = msg.data.cloumList;
            list = msg.data.list;
            var count = msg.data.count;
            $('#pager').pager({
                page: pageNum,
                recTotal: count
            });
        }
    });
    $("#serachBox").html("");
    //增加搜索项目
    for (var i = 0; i < cloums.length; i++) {
        var obj = cloums[i];
        if (obj["sechFlag"] == 1 &&
            (obj["en"] == "name" ||
                obj["en"] == "module" ||
                obj["en"] == "text"
            )) {
            var inpHtml = listTinputHtml.replaceAll("$ch$", obj["ch"]).replaceAll("$en$", obj["en"]);
            $("#serachBox").append(inpHtml);
        }
    }
    if (jsonMap != null) $.each(jsonMap, function (key, values) {
        $("#tinp_" + key).val(values);
    });


    //增加列表
    $("#ltr").html("");
    $("#lbody").html("");
    $("#ltr").append("<th>序号</th>");
    for (var i = 0; i < cloums.length; i++) {
        if (cloums[i].en != "text")
            $("#ltr").append("<th style='max-lines: 1'>" + cloums[i].ch + "</th>");
    }
    $("#ltr").append("<th style='min-width: 73px'>操作</th>");
    //循环便利铺值
    for (var i = 0; i < list.length; i++) {
        var trSht = "<tr><td>" + (i + 1 + ((pageNum - 1) * 10)) + "</td>";
        for (var j = 0; j < cloums.length; j++) {
            if (cloums[j].en != "text") {
                var xv = (list[i][cloums[j].en] == null ? "" : list[i][cloums[j].en]);
                trSht += "<td >" + xv + "</td>";
            }
        }
        trSht += (
            '<td><a href="#" onclick="showMarkDown(\'' + list[i].id + '\')" class="optMo" type="button">修改</a>&nbsp;&nbsp;'
            + '<a href="#" onclick="showMarkDown(\'' + list[i].id + '\',\'not\')" class="optMo" type="button">查看</a>&nbsp;&nbsp;'
            + '<a href="#" onclick="oepnDelete(\'common_begin_BeginMarkdown\',\'' + id + '\',\'' + list[i].id + '\')" class="optMo" type="button">删除</a>&nbsp;&nbsp;'
        );
        trSht += "</th>";
        trSht += "</tr>";
        $("#lbody").append(trSht);
    }

}

//新增文档
function addMakdown() {
    showMarkDown();
    mkid = -1;
}

function hideMkd() {
    window.location.reload();
}

var mkid = -1;

//保存文档
function bc() {
    var url = "/sys-console/begin/skip/save/common_begin_BeginMarkdown";
    var paramJ = {};
    paramJ["text"] = $("#markdText").val();
    paramJ["module"] = $("#mkd_mokuai").val();
    paramJ["name"] = $("#mkd_name").val();
    if (mkid != "-1") paramJ["id"] = mkid;
    $.ajax({
        type: "post",
        url: url,
        data: {
            paramJson: JSON.stringify(paramJ)
        },
        success(msg) {
            alert(msg["msg"]);
        }
    });
}

var ycx;

function showMarkDown(id, read) {

    ycx = editormd("test-editor", {
        path: "editormd/lib/",
        width: "100%",
        height: "100%",
        htmlDecode: "style,script,iframe",  // you can filter tags decode
        emoji: true,
        taskList: true,
        tex: true,  // 默认不解析
        flowChart: true,  // 默认不解析
        sequenceDiagram: true,  // 默认不解析
        toolbar: false,
        watch: true,
        onload: function () {
            if (read != null) {
                ycx.previewing();
                $(".editormd-preview-close-btn").hide();
            }
        }
    });
}

var mrText = "######接口名称：\n" +
    "######url:\n" +
    "######传参方式：\n" +
    "|字段名|类型|说明|是否必填|\n" +
    "|:----|:---|:----- |-----|\n" +
    "|示例|String|示例|否|\n" +
    "\n" +
    "######返回结构:\n" +
    "```\n" +
    "{\n" +
    "\tmsg:提示语,\n" +
    "\tcode:状态码（200正常）,\n" +
    "\tdata:结果集\n" +
    "}\n" +
    "```";
