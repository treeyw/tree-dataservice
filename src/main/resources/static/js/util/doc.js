$(function () {
    sysuri = window.location.pathname;
    sysuri = sysuri.substr(0, sysuri.indexOf("/html/"));
    uri = getUrlParam("uri");
    $.ajax({
        url: sysuri + "/api/docTags", type: "post", success(dataMsg) {
            //获取数据模拟
            var apiMap = dataMsg.data;
            var tagsNumId = 0;

            // 标签层（外层）
            localeSortKeys(apiMap).forEach(tagName => {
                tagsNumId++;
                const tagId = `tag_${tagsNumId}`;
                const tagMap = apiMap[tagName];

                // 标签HTML处理
                let tagsHtml = $("#tagsHtml").html();
                tagsHtml = tagsHtml
                    .replaceAll("#{标签id}", tagId)
                    .replaceAll("#{标签名}", tagName || "其他");
                $("#treeMenu").append(tagsHtml);
                let conNum = 0;
                // 控制层（中间层）
                localeSortKeys(tagMap).forEach(controllerName => {
                    conNum++;
                    const conId = `${tagId}_${conNum}_con_id`;
                    const apiList = tagMap[controllerName];

                    // 控制层HTML处理
                    let conHtml = $("#conHtml").html();
                    conHtml = conHtml
                        .replaceAll("#{控制层名}", controllerName)
                        .replaceAll("#{控制层id}", conId);
                    $(`#${tagId}`).append(conHtml);
                    // 接口列表排序（最内层）
                    apiList.sort((a, b) => a.name.localeCompare(b.name, 'zh-Hans-CN'));
                    // 接口HTML渲染
                    apiList.forEach(api => {
                        let apiHtml = $("#apiHtml").html();
                        apiHtml = apiHtml
                            .replaceAll("#{接口名}", api.name)
                            .replaceAll("#{接口全路径}", api.requestUrl);
                        $(`#${conId}`).append(apiHtml);
                    });
                });
            });

            $('#apiSearchInput').on('input', function () {
                const keyword = ($(this).val() || '').toString().trim().toLowerCase();
                const $results = $('#apiSearchResults');
                $results.empty().hide();

                if (!keyword) return;

                let matched = [];

                $('a.api-item').each(function () {
                    const text = $(this).text().toLowerCase(); // ✅ 正确获取 a 标签的文本
                    if (text.includes(keyword)) {
                        const $a = $(this);
                        const name = $a.text();
                        const conId = $a.closest('[id$="_con_id"]').attr('id');
                        const tagId = $a.closest('[id^="tag_"]').attr('id');

                        matched.push({
                            name, conId, tagId, target: $a
                        });
                    }
                });

                if (matched.length === 0) return;

                matched.forEach((item, index) => {
                    const $item = $(`
                <div style="padding:5px 10px; cursor:pointer;" class="search-result-item" data-index="${index}">
                  🔍 ${item.name}
                </div>
                `);
                    $results.append($item);
                });
                $results.show();
                $('.search-result-item').off('click').on('click', function () {
                    const index = $(this).data('index');
                    const item = matched[index];

                    // 高亮当前项
                    $('a.api-item').css('background', '');
                    item.target.css('background', 'yellow');

                    // ✅ 避免重复点击：判断是否已是当前 tab
                    if (!item.target.hasClass('active')) {
                        item.target.click();
                    }

                    // 自动展开所有父级菜单
                    const $li = item.target.closest('li');
                    $li.parents('li.has-list').each(function () {
                        const $toggle = $(this).children('.list-toggle');
                        if (!$toggle.hasClass('open')) {
                            $toggle.click();
                        }
                    });

                    // 滚动定位
                    $('html, body').animate({
                        scrollTop: item.target.offset().top - 100
                    }, 300);

                    $('#apiSearchResults').hide();
                });

            });
        }
    });
});

function localeSortKeys(obj) {
    return Object.keys(obj).sort((a, b) => a.localeCompare(b, 'zh-Hans-CN'));
}

// 定义标签页
var mytab;

function openApi(uri, name) {
    var tab = {
        title: name, url: 'docInfo.html?uri=' + uri, type: 'iframe',
    };
    if (mytab == null) {
        $('#tabsExample').tabs({tabs: [tab]});
        mytab = $("#tabsExample").data('zui.tabs');
        return;
    }
    mytab.open(tab);
}


