package cn.kunakun.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UploadResult {
    private Integer error;
    private String url;
}
