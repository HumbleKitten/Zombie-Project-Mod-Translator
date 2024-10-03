package cn.translation.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Config implements Serializable {

    private List<IdKey> baidu;
    private List<IdKey> tengxun;
}
