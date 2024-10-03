package cn.translation.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
public class IdKey implements Serializable {

    private String name;
    private String id;
    private String key;
}
