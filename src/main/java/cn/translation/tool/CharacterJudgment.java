package cn.translation.tool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CharacterJudgment {

    //是否包含中文
    public static boolean doesItContainChinese(String countname) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(countname);
        return m.find();
    }

    //是否包含英文
    public static boolean whetherToIncludeEnglish(String countname) {
        Pattern p = Pattern.compile("[a-zA-Z]");
        Matcher m = p.matcher(countname);
        return m.find();
    }

}
