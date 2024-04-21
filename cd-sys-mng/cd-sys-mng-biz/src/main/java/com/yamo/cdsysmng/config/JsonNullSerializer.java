package com.yamo.cdsysmng.config;

import cn.hutool.json.JSONNull;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

/**
 * 处理hutool的序列化问题
 */
@JsonComponent
public class JsonNullSerializer extends JsonSerializer<JSONNull> {
    @Override
    public void serialize(JSONNull value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeNull();
    }
}
