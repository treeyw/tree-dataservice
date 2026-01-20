package io.github.treeyw.dataservice.init;

import io.github.treeyw.dataservice.model.common.ConsoleLogBO;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @存放系统的公共信息
 */

public final class BlockAttribute {

    private BlockAttribute() {
    }

    //路径映射
    public static Map<String, String> LOG_URL_AND_METHOD_TEXT_MAP = new HashMap();
    public static Map<String, String> LOG_URL_AND_CLAZZ_TEXT_MAP = new HashMap();




    //下载临时文件路径前缀
    public static String TEMP_READ_PREFIX = null;
    //hive的分隔符
    public static String SPLIT_STR = "\001";

    public static LinkedHashMap<String, LinkedHashMap<String, List<ConsoleLogBO>>> DOC_TAGS_MAP = new LinkedHashMap<>();
    public static LinkedHashMap<String, ConsoleLogBO> DOC_URL_MAP = new LinkedHashMap<>();


}
