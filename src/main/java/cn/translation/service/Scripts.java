package cn.translation.service;

import cn.translation.api.TransApi;
import cn.translation.api.TencentTranslator;
import cn.translation.components.JTextPaneA;
import cn.translation.entity.IdKey;
import cn.translation.tool.CharacterJudgment;
import com.alibaba.fastjson2.JSON;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Scripts {

    public static Integer a1(List<File> allFileList, String translation, JTextPaneA jTextPane, IdKey idKey) {
        Integer num = 1;
        Integer length = 0;
        Integer lengthItem = 0;
        for (File file : allFileList) {
            Integer index = 0;
            long currentTime = System.currentTimeMillis();
            jTextPane.setText(jTextPane.getText()
                    +
                    "第[" + num + "/" + allFileList.size() + "]个文件:" + file.getName() + "已开始翻译" +
                    "\n"
            );
            String filePath = file.getPath();
            lengthItem = length;
            try {
                FileReader fileReader = null;
                StringBuilder sb = null;
                Scanner sc = null;
                String line = null;
                PrintWriter printWriter = null;
                sb = new StringBuilder();
                fileReader = new FileReader(filePath);
                sc = new Scanner(fileReader);
                while (sc.hasNextLine()) {  //按行读取字符串
                    line = sc.nextLine();
                    if (line.contains("DisplayName")) {
                        String query = line.substring(line.indexOf("=") + 1, line.length() - 1);
                        String name = query;
                        query = query.replaceAll("\t", "");
                        if (CharacterJudgment.doesItContainChinese(query)) {
                            sb.append(line).append("\n");
                            continue;
                        } else if (!CharacterJudgment.doesItContainChinese(query) && !CharacterJudgment.whetherToIncludeEnglish(query)) {
                            sb.append(line).append("\n");
                            continue;
                        }
                        if (name == null || name.isEmpty() || name.replaceAll(" ", "").isEmpty()) {
                            sb.append(line).append("\n");
                            continue;
                        }
                        char[] charArray = query.toCharArray();
                        char[] charArray1 = " []{}()-_=+|<>/\\*^%#~".toCharArray();
                        StringBuffer stringBuffer = new StringBuffer();
                        for (int i = 0; i < charArray.length; i++) {
                            char c = charArray[i];
                            boolean isUpperCase = true;
                            for (int j = i; j < charArray.length; j++) {
                                char c1 = charArray[j];
                                String s = Character.toString(c1);
                                boolean whetherToSkip = false;
                                for (char c2 : charArray1) {
                                    if (Character.toString(c2).equals(s)) {
                                        whetherToSkip = true;
                                        break;
                                    }
                                }
                                if (whetherToSkip) {
                                    break;
                                }
                                if (Character.isLowerCase(c1)) {
                                    isUpperCase = false;
                                }
                            }
                            if (Character.isUpperCase(c) && i != 0 && !isUpperCase) {
                                boolean beforeAndAfter = true;
                                char c1 = charArray[i - 1];
                                String s = Character.toString(c1);
                                for (char c2 : charArray1) {
                                    if (Character.toString(c2).equals(s)) {
                                        beforeAndAfter = false;
                                        break;
                                    }
                                }
                                if (beforeAndAfter) {
                                    stringBuffer.append(" ");
                                    stringBuffer.append(c);
                                } else {
                                    stringBuffer.append(c);
                                }
                            } else {
                                stringBuffer.append(c);
                            }
                        }
                        query = stringBuffer.toString();
                        if (translation.equals("1")) {
                            Thread.sleep(101);

                        } else if (translation.equals("2")) {
                            Thread.sleep(401);
                        }
                        long a = System.currentTimeMillis();
                        String dst = query;
                        if (translation.equals("1")) {
                            TransApi api = new TransApi();
                            String result = api.getTransResult(query, "auto", "zh", idKey);
                            HashMap<String, Object> parse = (HashMap<String, Object>) JSON.parse(result);
                            if (parse != null && parse.get("trans_result") == null) {
                                System.out.println(parse);
                                System.exit(1);
                            }
                            String string = parse.get("trans_result").toString();
                            string = string.substring(1, string.length() - 1);
                            HashMap<String, String> parse1 = (HashMap<String, String>) JSON.parse(string);
                            dst = parse1.get("dst");
                        } else if (translation.equals("2")) {
                            TencentTranslator tencentTranslator = new TencentTranslator();
                            String result = tencentTranslator.tencentTranslator(query, "auto", "zh", idKey);
                            HashMap<String, Object> parse = (HashMap<String, Object>) JSON.parse(result);
                            if (parse != null && parse.get("Response") == null) {
                                System.out.println(parse);
                                System.exit(1);
                            }
                            String string = parse.get("Response").toString();
                            HashMap<String, String> parse1 = (HashMap<String, String>) JSON.parse(string);
                            dst = parse1.get("TargetText");
                        }
                        index++;
                        length += query.length();
                        long b = System.currentTimeMillis();
                        line = line.replace(name, " " + dst);
                        jTextPane.setText(jTextPane.getText()
                                +
                                index + ":" + file.getName() + ",原始文本与翻译文本:{[" + query + "],[" + dst + "]},请求耗时:" + (double) (b - a) / 1000 + "秒" +
                                "\n"
                        );
                    } else if (line.contains("recipe ") || line.contains("recipe\t")) {
                        String query = line.substring(line.indexOf("recipe") + 6);
                        String name = query;
                        query = query.replaceAll(" ", "");
                        query = query.replaceAll("\t", "");
                        if (CharacterJudgment.doesItContainChinese(query)) {
                            sb.append(line).append("\n");
                            continue;
                        } else if (!CharacterJudgment.doesItContainChinese(query) && !CharacterJudgment.whetherToIncludeEnglish(query)) {
                            sb.append(line).append("\n");
                            continue;
                        }
                        if (name == null || name.isEmpty() || name.replaceAll(" ", "").isEmpty() || name.replaceAll("\t", "").isEmpty()) {
                            sb.append(line).append("\n");
                            continue;
                        }
                        char[] charArray = query.toCharArray();
                        char[] charArray1 = " []{}()-_=+|<>/\\*^%#~".toCharArray();
                        StringBuffer stringBuffer = new StringBuffer();
                        for (int i = 0; i < charArray.length; i++) {
                            char c = charArray[i];
                            boolean isUpperCase = true;
                            for (int j = i; j < charArray.length; j++) {
                                char c1 = charArray[j];
                                String s = Character.toString(c1);
                                boolean whetherToSkip = false;
                                for (char c2 : charArray1) {
                                    if (Character.toString(c2).equals(s)) {
                                        whetherToSkip = true;
                                        break;
                                    }
                                }
                                if (whetherToSkip) {
                                    break;
                                }
                                if (Character.isLowerCase(c1)) {
                                    isUpperCase = false;
                                }
                            }
                            if (Character.isUpperCase(c) && i != 0 && !isUpperCase) {
                                boolean beforeAndAfter = true;
                                char c1 = charArray[i - 1];
                                String s = Character.toString(c1);
                                for (char c2 : charArray1) {
                                    if (Character.toString(c2).equals(s)) {
                                        beforeAndAfter = false;
                                        break;
                                    }
                                }
                                if (beforeAndAfter) {
                                    stringBuffer.append(" ");
                                    stringBuffer.append(c);
                                } else {
                                    stringBuffer.append(c);
                                }
                            } else {
                                stringBuffer.append(c);
                            }
                        }
                        query = stringBuffer.toString();
                        if (translation.equals("1")) {
                            Thread.sleep(101);

                        } else if (translation.equals("2")) {
                            Thread.sleep(401);
                        }
                        long a = System.currentTimeMillis();
                        String dst = query;
                        if (translation.equals("1")) {
                            TransApi api = new TransApi();
                            String result = api.getTransResult(query, "auto", "zh", idKey);
                            HashMap<String, Object> parse = (HashMap<String, Object>) JSON.parse(result);
                            if (parse != null && parse.get("trans_result") == null) {
                                System.out.println(parse);
                                System.exit(1);
                            }
                            String string = parse.get("trans_result").toString();
                            string = string.substring(1, string.length() - 1);
                            HashMap<String, String> parse1 = (HashMap<String, String>) JSON.parse(string);
                            dst = parse1.get("dst");
                        } else if (translation.equals("2")) {
                            TencentTranslator tencentTranslator = new TencentTranslator();
                            String result = tencentTranslator.tencentTranslator(query, "auto", "zh", idKey);
                            HashMap<String, Object> parse = (HashMap<String, Object>) JSON.parse(result);
                            if (parse != null && parse.get("Response") == null) {
                                System.out.println(parse);
                                System.exit(1);
                            }
                            String string = parse.get("Response").toString();
                            HashMap<String, String> parse1 = (HashMap<String, String>) JSON.parse(string);
                            dst = parse1.get("TargetText");
                        }
                        index++;
                        length += query.length();
                        long b = System.currentTimeMillis();
                        line = line.replace(name, " " + dst);
                        jTextPane.setText(jTextPane.getText()
                                +
                                index + ":" + file.getName() + ",原始文本与翻译文本:{[" + query + "],[" + dst + "]},请求耗时:" + (double) (b - a) / 1000 + "秒" +
                                "\n"
                        );
                    }
                    sb.append(line).append("\n");
                }
                if (!(translation.equals("1") || translation.equals("2"))) {
                    printWriter = new PrintWriter(new FileWriter(filePath));
                    printWriter.print(sb);
                    printWriter.close();
                }
                fileReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            long endTime = System.currentTimeMillis();
            jTextPane.setText(jTextPane.getText()
                    +
                    "第[" + num + "/" + allFileList.size() + "]个文件:" + file.getName() + "已翻译完成,翻译次数:" + index + ",耗时:" + (double) (endTime - currentTime) / 1000 + "秒,本文件翻译字符数:" + (length - lengthItem) +
                    "\n"
            );
            num++;
        }
        jTextPane.setText(jTextPane.getText()
                +
                "项目文件文件共有" + allFileList.size() + "个文件,已全部翻译,翻译总字符数:" + length +
                "\n"
        );
        return length;
    }

}
