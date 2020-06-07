package com.demo.akscosmos.control;

import java.util.Objects;

import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.azure.cosmos.CosmosClientException;
import com.azure.cosmos.CosmosContainerProperties;
import com.azure.cosmos.CosmosContainerRequestOptions;
import com.azure.cosmos.CosmosContainerResponse;
import com.azure.cosmos.CosmosDatabaseResponse;
import com.azure.cosmos.CosmosItem;
import com.azure.cosmos.CosmosItemRequestOptions;
import com.azure.cosmos.CosmosItemResponse;
import com.azure.cosmos.PartitionKey;
import com.demo.akscosmos.config.AksCosmosConfig;
import com.demo.akscosmos.domain.Data;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("akscosmosdemo")
@Log4j2
public class AksCosmosController {

	@Autowired
	private AksCosmosConfig aksCosmosConfig;
	@Autowired
	private ObjectMapper objectMapper;

	@PutMapping("/save")
	public int saveData(@RequestBody String request)
			throws CosmosClientException, JsonMappingException, JsonProcessingException {

		Data data = objectMapper.readValue(request, Data.class);
		Assert.notNull(data, "Data must not be null");
		log.log(Level.INFO, "RequestReceived {} ", request);
		CosmosDatabaseResponse dbResponse = aksCosmosConfig.getCosmosClient()
				.createDatabaseIfNotExists(aksCosmosConfig.getDatabaseName());
		log.log(Level.INFO, "Db exist/created {} ", dbResponse.getDatabase().getId());
		CosmosContainerProperties containerProperties = new CosmosContainerProperties(aksCosmosConfig.getContainerName(),
				"/category");
		CosmosContainerResponse containerResponse = dbResponse.getDatabase()
				.createContainerIfNotExists(containerProperties);
		log.log(Level.INFO, "Container exist/created {} ", containerResponse.getContainer().getId());
		CosmosItemRequestOptions cosmosItemRequestOptions = new CosmosItemRequestOptions(data.getCategory());
		CosmosItemResponse cosmosItemResponse = containerResponse.getContainer().createItem(data,
				cosmosItemRequestOptions);
		return cosmosItemResponse.getStatusCode();

	}

	@GetMapping("/get/{id}")
	public CosmosItem getDocumentById(@PathVariable String id) throws CosmosClientException {
		CosmosItem cosmosItem = aksCosmosConfig.getCosmosClient().getDatabase(aksCosmosConfig.getDatabaseName())
				.getContainer(aksCosmosConfig.getContainerName()).getItem(id, "beauty");
		CosmosItemRequestOptions options = new CosmosItemRequestOptions("1");
		CosmosItemResponse response = cosmosItem.read(options);
		return response.getItem();
		
		
		
		

	}

}
