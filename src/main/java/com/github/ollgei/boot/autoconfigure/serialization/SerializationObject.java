package com.github.ollgei.boot.autoconfigure.serialization;

import lombok.Builder;
import lombok.Getter;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
@Getter
@Builder
public class SerializationObject {
    /**版本*/
    @Builder.Default
    private Integer version = 1;
    /**object*/
    private Object object;
}
