package ee.kristofer.rental.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RedisHash("Auth")
@Accessors(chain = true)
@Data
public class Auth implements Serializable {

    @Id
    private String apiKey;
    private String userId;

    public void setApiKey(String apiKey) {
        this.apiKey = new String(Base64.getEncoder().encode(apiKey.getBytes(StandardCharsets.UTF_8)));
    }
}
