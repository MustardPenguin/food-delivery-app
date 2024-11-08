package com.food.delivery.app.common.utility.datetime;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class DateTimeUtil {

    public LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now(ZoneId.of("UTC"));
    }
}
