package com.tasfe.zh.base.exception;

import com.tasfe.zh.base.model.code.ErrorCode;
import com.tasfe.zh.base.model.response.ResponseData;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * @author liuxiang
 */
public final class ResponseDataBuilder {
    /**
     * 返回成功.
     */
    public static final String RESPONSE_OK = "1";
    /**
     * 返回成功.
     */
    public static final String RESPONSE_FAIL = "0";

    /**
     * 构造一个失败响应.<br>
     * 用于构造一个比较用见或是常用的失败响应。<br>
     *
     * @param enums 常见错误枚举类的一个实例.<br>
     * @return
     */
    public static <T> ResponseData<T> buildErrorResponse(ErrorCode enums) {
        ResponseData<T> entity = new ResponseData<>();
        //entity.setStatus(RESPONSE_FAIL);
        //entity.setError(String.valueOf(enums.getCode()));
        entity.setMsg(enums.getMessage());
        return entity;
    }

//	/**
//	 * 构造一个失败响应.<br>
//	 *
//	 * @param enums
//	 * @return
//	 */
//	@Deprecated
//	public static ResponseData<String> buildErrorResponse(ErrorCode enums) {
//		ResponseData<String> entity = new ResponseData<String>();
//		entity.setStatus(RESPONSE_FAIL);
//		entity.setError(String.valueOf(enums.getCode()));
//		entity.setMsg(enums.getMessage());
//		return entity;
//	}

    /**
     * 构造一个失败响应.<br>
     *
     * @param error   失败错误编码.<br>
     * @param message 失败错误说明.<br>
     * @return
     */
    public static <T> ResponseData<T> buildErrorResponse(String error, String message) {
        ResponseData<T> entity = new ResponseData<>();
        //entity.setStatus(RESPONSE_FAIL);
        if (StringUtils.isBlank(error) || !StringUtils.isNumeric(error)) {
            error = "42000001";
        }
        //entity.setError(error);
        entity.setMsg(message);
        return entity;
    }

    /**
     * 构造一个正常响应.<br>
     * <p>
     * 响应数据.<br>
     *
     * @return
     */
    public static <T> ResponseData<T> buildNormalResponse() {
        //规范：所有正常请求(status="1",error="00000000"),code与pageCount属性被废弃.
        //return new ResponseData<T>(RESPONSE_OK, "00000000");
        return null;
    }

    /**
     * 构造一个正常响应.<br>
     *
     * @param data 响应数据.<br>
     * @return
     */
    public static <T> ResponseData<T> buildNormalResponse(T data) {
        ResponseData<T> entity = buildNormalResponse();
        entity.setData(data);
        return entity;
    }

//	public static <T> ResponseData<Map<String,T>> buildNormalResponse(String key,T data) {
//		Map<String,T> result = new HashMap<>();
//		result.put(key, data);
//		
//		ResponseData<Map<String,T>> entity = buildNormalResponse();
//		entity.setData(result);
//		return entity;
//	}

    public static <T> Boolean isSuccess(ResponseData<T> entity) {
        //return entity.getStatus().equals(RESPONSE_OK);
        return null;
    }

    public static <T> Boolean isSuccess2(ResponseData<T> entity) {
        //return entity.getStatus().equals(RESPONSE_OK) && entity.getError().equals("00000000");
        return null;
    }

    /**
     * 适用于接口返回data为null时业务不成功的场景
     */
    public static <T> Boolean isSuccess3(ResponseData<T> entity) {
        //return entity.getStatus().equals(RESPONSE_OK) && entity.getError().equals("00000000") && entity.getData() != null;
        return null;
    }

    public static <T extends Serializable> T getEntity(ResponseData<T> entity) {
        if (isSuccess(entity)) {
            return (T) entity.getData();

        }
        return null;
    }

}
