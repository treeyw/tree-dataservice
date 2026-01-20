var paramObj = {};
var uri;
var sysuri;
$(function () {
    var docUrl = "/api/docByUrl";
    sysuri = window.location.pathname;
    if (sysuri.includes("docTagsHaveEntity")) docUrl = "/api/docTagsHaveEntity";
    sysuri = sysuri.substr(0, sysuri.indexOf("/html/"));
    uri = getUrlParam("uri");
    $.ajax({
        url: sysuri + docUrl,
        data: {
            url: uri
        },
        type: "post",
        success(data) {
            paramObj = data.data;
            $("#urlInp").val(paramObj.requestUrl);
            var method = paramObj.requestMethod;
            if (method == null) {
                $("#methodInp").val("post");
            } else {
                method = method.replace("Mapping", "");
                $("#methodInp").val(method);
            }
            paramObj.enterParameters.forEach(n => {
                var html = $("#inpHtml").html();
                var type = "text";
                if (n.type.indexOf("File") > -1 || n.type.indexOf("file") > -1) {
                    type = "file";
                }
                html = html.replaceAll("#{key}", n.name);
                html = html.replaceAll("#{说明}", n.explain);
                html = html.replaceAll("#{type}", type);
                html = html.replaceAll("#{val}", n.example);
                $("#formBox").append(html);
                $("#" + n.name + "_type_" + type).attr("selected", "selected");
            });
        }
    });
});

function onParam(opt) {
    $(".yh-btn").removeClass("yh-active");
    $("#" + opt).addClass("yh-active");
    if (opt == "doc") {
        window.open("docStr.html?uri=" + uri);
    }
}

function inpType(typeid, valid) {
    var type = $("#" + typeid).val();
    $("#" + valid).attr("type", type);
}

function onchangeKey(kid, nameid) {
    $("#" + nameid).attr("name", $("#" + kid).val());
}

function send() {

    $("#formBox").attr("action", $("#urlInp").val());
    $("#formBox").attr("method", $("#methodInp").val());

    var formData = $('#formBox').serializeArray();
    var jsonData = {};

    $.each(formData, function (index, field) {
        if (field.value != null && field.value != "")
            jsonData[field.name] = field.value;
    });

    $.ajax({
        url: $("#urlInp").val(),
        type: $("#methodInp").val(),
        //contentType: "application/x-www-form-urlencoded",
        contentType: false, // 不设置内容类型
        processData: false, // 不处理数据
        data: new FormData($('#formBox')[0]), // 使用 FormData 对象
        success(msg) {
            $("#sendMsg").text(JSON.stringify(msg, null, '\t'));
        }
    });

}
