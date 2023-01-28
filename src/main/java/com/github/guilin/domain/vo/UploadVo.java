package com.github.guilin.domain.vo;

import com.github.guilin.common.Constant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 上传文件后响应结果对象
 *
 * @author CaoChenLei
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadVo {
    private Integer code;
    private String msg;
    private String fileName;
    private String type;
    private Long size;
    private String url;
    private String key;

    public static UploadVo fail(String msg) {
        return new UploadVo(Constant.RESPONSE_FAIL, msg, null, null, null, null, null);
    }
}
