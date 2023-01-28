package com.github.guilin.domain.vo;

import com.github.guilin.common.Constant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 统一处理返回结果
 *
 * @author CaoChenLei
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultVo implements Serializable {
    private Integer code;
    private String msg;
    private Object data;

    public static ResultVo success() {
        return new ResultVo(Constant.RESPONSE_SUCCESS, "success", null);
    }

    public static ResultVo success(String msg) {
        return new ResultVo(Constant.RESPONSE_SUCCESS, msg, null);
    }

    public static ResultVo successWith(Object data) {
        return new ResultVo(Constant.RESPONSE_SUCCESS, "success", data);
    }

    public static ResultVo successWith(Integer code, String msg) {
        return new ResultVo(code, msg, null);
    }

    public static ResultVo successWith(String msg, Object data) {
        return new ResultVo(Constant.RESPONSE_SUCCESS, msg, data);
    }

    public static ResultVo fail() {
        return new ResultVo(Constant.RESPONSE_FAIL, "fail", null);
    }

    public static ResultVo fail(String msg) {
        return new ResultVo(Constant.RESPONSE_FAIL, msg, null);
    }

    public static ResultVo failWith(Object data) {
        return new ResultVo(Constant.RESPONSE_FAIL, "fail", data);
    }

    public static ResultVo failWith(Integer code, String msg) {
        return new ResultVo(code, msg, null);
    }

    public static ResultVo failWith(String msg, Object data) {
        return new ResultVo(Constant.RESPONSE_FAIL, msg, data);
    }

}
