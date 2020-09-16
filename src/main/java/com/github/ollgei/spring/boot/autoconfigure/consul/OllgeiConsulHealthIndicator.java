/*
 * Copyright 2013-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.ollgei.spring.boot.autoconfigure.consul;

import java.util.List;
import java.util.Map;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.catalog.CatalogServicesRequest;

/**
 * @author Spencer Gibb
 */
public class OllgeiConsulHealthIndicator extends AbstractHealthIndicator {

	private ConsulClient consul;

	public OllgeiConsulHealthIndicator(ConsulClient consul) {
		this.consul = consul;
	}

	@Override
	protected void doHealthCheck(Health.Builder builder) throws Exception {
		final Response<String> leaderStatus = this.consul.getStatusLeader();
		final Response<Map<String, List<String>>> services = this.consul
				.getCatalogServices(CatalogServicesRequest.newBuilder()
						.setQueryParams(QueryParams.DEFAULT).build());
		builder.up().withDetail("leader", leaderStatus.getValue()).withDetail("services",
				services.getValue());
	}

}
