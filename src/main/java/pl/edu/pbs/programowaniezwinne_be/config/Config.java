package pl.edu.pbs.programowaniezwinne_be.config;

import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchClients;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;


@Configuration
public class Config extends ElasticsearchConfiguration {
    @Value("${elastic.url}")
    private String elasticURl;

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(elasticURl)
                .withClientConfigurer(
                        ElasticsearchClients.ElasticsearchRestClientConfigurationCallback.from(
                                httpClientBuilder -> httpClientBuilder
                                        .setDefaultHeaders(
                                                new BasicHeader[]
                                                        {new BasicHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString())})
                        )
                )
                .build();
    }


}