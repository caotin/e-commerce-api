package com.challenge.ecommerce.users.models.location;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import java.util.ArrayList;
import java.util.List;

@RedisHash("Province")
@Getter
@Setter
public class Province extends BaseLocation {
  private List<District> districts = new ArrayList<>();
}
