package cn.cjsj.im.gty.http;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;

import cn.cjsj.im.gty.bean.ErrorResult;
import cn.cjsj.im.gty.bean.HttpResult;
import cn.cjsj.im.gty.common.ConstantValue;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by LuoYang on 2016/2/23.
 * 结果集处理
 */
class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final Type type;

    GsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
            Log.d("NetworkResponse", "response>>" + response);
            Log.d("JPUSH_REGISTER_ID>>",  ConstantValue.JPUSH_REGISTER_ID);
            //httpResult 只解析result字段
            HttpResult httpResult = gson.fromJson(response, HttpResult.class);
            //
            if (httpResult.getStatus() .equals("ERROR")) {
                ErrorResult errorResult = gson.fromJson(response, ErrorResult.class);
                Log.d("ErrorResult", "ERROR>>" + errorResult.getData());
                throw new ApiException(100,errorResult.getData());
            }
            return gson.fromJson(response, type);


    }
}
