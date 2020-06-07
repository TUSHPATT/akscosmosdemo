package com.demo.akscosmos.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.azure.cosmos.ConnectionMode;
import com.azure.cosmos.ConnectionPolicy;
import com.azure.cosmos.ConsistencyLevel;
import com.azure.cosmos.CosmosClient;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.CosmosClientException;
import com.azure.cosmos.CosmosDatabaseProperties;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.Lists;

@Configuration("aksCosmosConfig")
public class AksCosmosConfig {
	@Value("${cosmos.db.host}")
	private String dbHost;
	@Value("${cosmos.db.name}")
	private String dbName;
	@Value("${cosmos.db.container.name}")
	private String containerName;
	@Value("${cosmos.db.account.key}")
	private String accountKey;

	@Bean("cosmosClient")
	public CosmosClient getCosmosClient() throws CosmosClientException {
		ConnectionPolicy defaultPolicy = ConnectionPolicy.getDefaultPolicy();
		defaultPolicy.setUserAgentSuffix("AksCosmosDemoAgent");
		defaultPolicy.setPreferredLocations(Lists.newArrayList("East US"));
		return CosmosClient.builder().setEndpoint(dbHost).setKey(accountKey).setConnectionPolicy(defaultPolicy)
				.setConsistencyLevel(ConsistencyLevel.EVENTUAL).buildClient();

	}

	@Bean("objectMapper")

	public ObjectMapper getMapper()

	{
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.disable(DeserializationFeature.UNWRAP_ROOT_VALUE);
		mapper.setSerializationInclusion(Include.NON_NULL);
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		return mapper;

	}

	public String getContainerName() {
		return containerName;
	}

	public String getDatabaseName() {
		return dbName;
	}

}
