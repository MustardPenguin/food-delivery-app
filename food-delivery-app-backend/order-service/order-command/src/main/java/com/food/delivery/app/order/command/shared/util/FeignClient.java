package com.food.delivery.app.order.command.shared.util;

@org.springframework.cloud.openfeign.FeignClient(value = "feignClient", url = "${apiGatewayHost}")
public interface FeignClient {


}
