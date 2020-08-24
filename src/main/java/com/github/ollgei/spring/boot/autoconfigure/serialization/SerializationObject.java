package com.github.ollgei.spring.boot.autoconfigure.serialization;

import lombok.Getter;
import lombok.Setter;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
@Getter
@Setter
public class SerializationObject {
    /**版本*/
    private Integer version = 1;
    /**object*/
    private Object object;
}
