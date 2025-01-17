package com.swallow.fly.http

/**
 * @Description:
 * @Author:   Hsp
 * @Email:    1101121039@qq.com
 * @CreateTime:     2022/2/14 17:11
 * @UpdateRemark:   更新说明：
 */
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.Type

class TimeoutCallAdapterFactory : CallAdapter.Factory() {
    companion object {
        fun create(): TimeoutCallAdapterFactory {
            return TimeoutCallAdapterFactory()
        }
    }

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        val timeout = annotations.firstOrNull { it is Timeout } as? Timeout
        val delegate = retrofit.nextCallAdapter(this, returnType, annotations)

        if (getRawType(returnType) != Call::class.java) {
            return null
        }

        return object : CallAdapter<Any, Call<Any>> {
            override fun responseType(): Type {
                return delegate.responseType()
            }

            override fun adapt(call: Call<Any>): Call<Any> {
                if (timeout != null) {
                    call.timeout().timeout(timeout.value, timeout.unit)
                }
                return call
            }
        }
    }
}