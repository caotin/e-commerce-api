package com.challenge.ecommerce.users.models.location;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.util.ArrayList;
import java.util.List;

@RedisHash("District")
@Getter
@Setter
public class District extends BaseLocation{
    private String provinceId;
    private List<Ward> wards = new ArrayList<>();
}
