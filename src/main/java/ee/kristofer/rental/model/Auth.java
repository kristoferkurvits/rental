package ee.kristofer.rental.model;

import lombok.experimental.Accessors;
import org.springframework.data.redis.core.RedisHash;

import java.util.UUID;

@RedisHash("Auth")
@Accessors(chain = true)
public class Auth {
    private String apiKey;
    private UUID userId;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
