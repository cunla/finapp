package com.delirium.finapp.client;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import retrofit.Converter;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

final class StringConverterFactory extends Converter.Factory {
    private static final MediaType MEDIA_TYPE = MediaType.parse("text/plain");

    public static Converter.Factory create() {
        return new StringConverterFactory();
    }

    @Override
    public Converter<?, RequestBody> toRequestBody(Type type, Annotation[] annotations) {
        if (String.class.equals(type))// || (type instanceof Class && ((Class<?>) type).isEnum()))
        {
            return new Converter<String, RequestBody>() {
                @Override
                public RequestBody convert(String value) throws IOException {
                    return RequestBody.create(MEDIA_TYPE, value);
                }
            };
        }
        return null;
    }
}
