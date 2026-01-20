var paramObj;
$(function () {
    sysuri = window.location.pathname;
    sysuri = sysuri.substr(0, sysuri.indexOf("/html/"));
    uri = getUrlParam("uri");
    $.ajax({
        url: sysuri + "/api/docByUrl",
        data: {
            url: uri
        },
        type: "post",
        success(data) {
            paramObj = data.data;
            var titleHtmlStr = $("#titleHtml").html();
            titleHtmlStr = titleHtmlStr.replaceAll("#{接口名}", paramObj.name);
            var method = "post";
            if (paramObj.requestMethod != "RequestMapping")
                method = paramObj.requestMethod.replace("Mapping", "");

            titleHtmlStr = titleHtmlStr.replaceAll("#{方式}", method);
            titleHtmlStr = titleHtmlStr.replaceAll("#{url}", paramObj.requestUrl);
            titleHtmlStr = titleHtmlStr.replaceAll("#{说明}", paramObj.notes);
            $("#titleBox").append(titleHtmlStr);

            paramObj.enterParameters.forEach(n => {
                var trHtml = $("#docHtml").html();
                trHtml = trHtml.replaceAll("#{name}", n.name);
                var type = n.type.split(".");
                trHtml = trHtml.replaceAll("#{type}", type[type.length - 1]);
                trHtml = trHtml.replaceAll("#{paramType}", n.paramType);
                trHtml = trHtml.replaceAll("#{required}", n.required);
                trHtml = trHtml.replaceAll("#{example}", n.example);
                trHtml = trHtml.replaceAll("#{explain}", n.explain);
                $("#tableBody").append(trHtml);
            });
            var fc = "{\n" +
                "\t\"msg\": \"系统正常\",\n" +
                "\t\"code\": \"200\",\n" +
                "\t\"data\": {}\n" +
                "}";
            if (paramObj.outParameters != null && paramObj.outParameters.length > 1)
                fc = paramObj.outParameters;
            var bottomHtmlStr = $("#bottomHtml").html();

            bottomHtmlStr = bottomHtmlStr.replace("#{返参说明}", JSON.stringify(JSON.parse(fc), null, '\t'));
            $("#docBox").append(bottomHtmlStr);
        }
    });
});
