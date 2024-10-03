package cn.translation.tool;

import java.io.File;
import java.util.List;

public class GetAllFile {

    public static void getAllFile(File fileInput, List<File> translateList, List<File> itemList, Boolean target, Integer num) {
        if (!num.equals(1) &&
//                fileInput.getName().equals("CN") &&//                fileInput.getName().equals("EN") &&
                fileInput.getName().equals("Translate")
        ) {
            if (
//                    fileInput.getName().equals("CN") ||
                    fileInput.getName().equals("EN") ||
                            fileInput.getName().equals("Translate")) {
                getAllFile(fileInput, translateList, itemList, true, 1);
            }
        } else if (!num.equals(2) && fileInput.getName().equals("scripts")) {
            getAllFile(fileInput, translateList, itemList, true, 2);
        } else {
            File[] fileList = fileInput.listFiles();
            assert fileList != null;
            for (File file : fileList) {
                if (file.isDirectory()) {
                    // 递归处理文件夹
                    // 如果不想统计子文件夹则可以将下一行注释掉
                    String fileName = file.getName();
                    if (target || fileName.equals("Translate") || fileName.equals("scripts")) {
                        if (num.equals(1) || fileName.equals("Translate")) {
                            if (
//                                fileName.equals("CN")
                                    fileName.equals("EN")
                                            || fileName.equals("Translate")) {
                                getAllFile(file, translateList, itemList, true, 1);
                            }
                        } else if (num.equals(2) || fileName.equals("scripts")) {
                            getAllFile(file, translateList, itemList, true, 2);
                        }
                    } else {
                        getAllFile(file, translateList, itemList, false, 0);
                    }
                } else {
                    // 如果是文件则将其加入到文件数组中
                    if (target) {
                        if (num.equals(1)) {
                            translateList.add(file);
                        } else if (num.equals(2)) {
                            itemList.add(file);
                        }
                    }
                }
            }
        }
    }

}
