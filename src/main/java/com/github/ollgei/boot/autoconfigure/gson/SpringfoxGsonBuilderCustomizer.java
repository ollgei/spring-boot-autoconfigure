package com.github.ollgei.boot.autoconfigure.gson;

import com.github.ollgei.base.commonj.gson.GsonBuilder;
import com.github.ollgei.boot.autoconfigure.gson.spring.SpringfoxJsonToGsonAdapter;
import springfox.documentation.spring.web.json.Json;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
public class SpringfoxGsonBuilderCustomizer implements  OllgeiGsonBuilderCustomizer {
    @Override
    public void customize(GsonBuilder gsonBuilder) {
        gsonBuilder.registerTypeAdapter(Json.class, new SpringfoxJsonToGsonAdapter());
    }
}
