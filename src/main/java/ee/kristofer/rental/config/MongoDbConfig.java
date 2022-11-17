package ee.kristofer.rental.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;


@Configuration
@PropertySource("classpath:application-localdev.properties")
public class MongoDbConfig extends AbstractMongoClientConfiguration {
    private static final Logger log = LogManager.getLogger(MongoDbConfig.class);

    @Value("${mongodb.database.table}")
    private String tableName;

    @Value("${mongodb.connection.string}")
    private String connectionString;


    @Override
    protected String getDatabaseName() {
        return tableName;
    }

    @Override
    public MongoClient mongoClient() {

        var dbConnection = connectionString + tableName;
        log.debug("Using database connection string: {}", dbConnection);
        final ConnectionString connectionString = new ConnectionString(dbConnection);
        final MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        return MongoClients.create(mongoClientSettings);
    }
}
