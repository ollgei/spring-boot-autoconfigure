package com.github.ollgei.boot.autoconfigure.httpclient;

import java.util.List;

import feign.Feign;
import feign.Logger;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
public class FeinBuilderHelper {

    public static Feign.Builder builder(LoggerType loggerType, FeignLoggerLevel level,
                                        Feign.Builder builder,
                                        List<FeignCustomizer> customizers) {
        final Logger logger;
        final Logger.Level loggerLevel;
        switch (loggerType) {
            case CONSOLE:
                logger = new Logger.ErrorLogger();
                break;
            default:
                logger = new Logger.NoOpLogger();
        }

        switch (level) {
            case BASIC:
                loggerLevel = Logger.Level.BASIC;
                break;
            case HEADERS:
                loggerLevel = Logger.Level.HEADERS;
                break;
            case FULL:
                loggerLevel = Logger.Level.FULL;
                break;
            default:
                loggerLevel = Logger.Level.NONE;
        }

        final Feign.Builder custom = builder
                .logLevel(loggerLevel)
                .logger(logger);

        customizers.forEach(customizer -> customizer.customize(builder));

        return custom;
    }

}
