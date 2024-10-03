package cn.translation.tool;

import cn.translation.entity.Config;
import cn.translation.main.Windows;
import com.alibaba.fastjson2.JSON;
import lombok.Data;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Scanner;

@Data
public class GetConfig {

    private String filePath;

    public GetConfig() {
        filePath = Windows.class.getResource("/config.txt").getPath();
    }

    public Config getConfig() {
        File config = new File(filePath);
        StringBuilder sb = new StringBuilder();
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(config);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Scanner sc = new Scanner(fileReader);
        while (sc.hasNextLine()) {
            sb.append(sc.nextLine());
        }
        try {
            fileReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return JSON.parseObject(sb.toString(), Config.class);
    }

}
