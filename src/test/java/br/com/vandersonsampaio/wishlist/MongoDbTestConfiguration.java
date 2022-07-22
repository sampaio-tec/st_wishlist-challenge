package br.com.vandersonsampaio.wishlist;

import com.mongodb.client.MongoClients;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.*;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.util.SocketUtils;

import java.io.IOException;

@Configuration
public class MongoDbTestConfiguration {

    private static final String MONGO_DB_URL = "mongodb://%s:%d";
    private static final String MONGO_DB_NAME = "embeded_db";
    private MongodExecutable mongodExecutable;

    @Bean
    public MongoTemplate mongoTemplate() throws IOException {
        int randomPort = SocketUtils.findAvailableTcpPort();;

        final Version version = Version.V3_2_20;

        MongodConfig mongodConfig = MongodConfig.builder().version(version)
                .net(new Net(randomPort, Network.localhostIsIPv6()))
                .build();

        MongodStarter starter = MongodStarter.getDefaultInstance();
        mongodExecutable = starter.prepare(mongodConfig);
        mongodExecutable.start();
        return new MongoTemplate(MongoClients.create(String.format(MONGO_DB_URL, "localhost", randomPort)),MONGO_DB_NAME);
    }
}
