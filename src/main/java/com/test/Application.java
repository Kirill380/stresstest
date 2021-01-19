package com.test;

import java.util.concurrent.TimeUnit;

import lombok.Value;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class Application {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ExpiringMap<Long, UserDto> cache = ExpiringMap.builder().variableExpiration().build();
    private ExpiringMap<Long, ValueWithTime> probalCache = ExpiringMap.builder().variableExpiration().build();


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @GetMapping(value = "/v1/users/{id}", produces = "application/json")
    public UserDto getUser(@PathVariable("id") Long userId) {
        return getWithProbalCache(userId);
    }


    private UserDto get(Long userId) {
       return jdbcTemplate.queryForObject(
                  "select * from users WHERE id = ?",
                  new Object[]{userId},
                  (rs, rowNum) ->
                        new UserDto(
                              rs.getLong("id"),
                              rs.getString("name"),
                              rs.getString("email")
                        )
            );
    }

    private UserDto getWithCache(Long userId) {
        if (cache.containsKey(userId)) {
            return cache.get(userId);
        } else {
            final UserDto userDto = jdbcTemplate.queryForObject(
                  "select * from users WHERE id = ?",
                  new Object[]{userId},
                  (rs, rowNum) ->
                        new UserDto(
                              rs.getLong("id"),
                              rs.getString("name"),
                              rs.getString("email")
                        )
            );
            cache.put(userId, userDto, ExpirationPolicy.CREATED, 2, TimeUnit.MINUTES);
            return userDto;
        }
    }

    private UserDto getWithProbalCache(Long userId) {
        final ValueWithTime valueWithTime = probalCache.get(userId);
        if (valueWithTime == null ||  - 1 * valueWithTime.delta * Math.log(RandomUtils.nextDouble(0.01, 1)) >= probalCache.getExpectedExpiration(userId)) {
            final long start = System.currentTimeMillis();
            final UserDto userDto = jdbcTemplate.queryForObject(
                  "select * from users WHERE id = ?",
                  new Object[]{userId},
                  (rs, rowNum) ->
                        new UserDto(
                              rs.getLong("id"),
                              rs.getString("name"),
                              rs.getString("email")
                        )
            );
            final long end = System.currentTimeMillis();
            probalCache.put(userId, new ValueWithTime(userDto, end - start), ExpirationPolicy.CREATED, 2, TimeUnit.MINUTES);
            return userDto;
        }
        return valueWithTime.userDto;
    }


    @Value
    class ValueWithTime {
         UserDto userDto;
         Long delta;
    }
}
