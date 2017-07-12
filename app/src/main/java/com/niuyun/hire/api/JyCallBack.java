package com.niuyun.hire.api;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sun.luwei on 2016/11/23.
 * 响应 统一封装
 */

public abstract class JyCallBack<T> implements Callback<T> {


    @Override
    public void onResponse(Call<T> call, Response<T> response) {

        //do something
        if(null !=response && 200== response.code()){
            onSuccess(call, response);
        }else{
            onError(call, response);
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        //do something
        t.printStackTrace();
        onError(call, t);
    }

    public abstract void onSuccess(Call<T> call, Response<T> response);

    public abstract void onError(Call<T> call, Throwable t);

    public abstract void onError(Call<T> call, Response<T> response);


}
