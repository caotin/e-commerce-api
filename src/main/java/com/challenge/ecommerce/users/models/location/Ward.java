package com.challenge.ecommerce.users.models.location;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("Ward")
@Getter
@Setter
public class Ward extends BaseLocation {
  private String districtId;
}
