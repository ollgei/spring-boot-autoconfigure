package com.github.ollgei.boot.autoconfigure.httpclient;

import org.apache.http.impl.client.HttpClientBuilder;

/**
 * desc.
 * @author ollgei
 * @since 1.0
 */
@FunctionalInterface
public interface HttpClientBuilderCustomizer {

	/**
	 * Customize the {@link HttpClientBuilder}.
	 * @param builder the builder to customize
	 */
	void customize(HttpClientBuilder builder);

}