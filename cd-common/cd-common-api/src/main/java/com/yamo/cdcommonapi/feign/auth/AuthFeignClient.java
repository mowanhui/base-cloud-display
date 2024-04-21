package com.yamo.cdcommonapi.feign.auth;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(contextId = "AuthFeignClient",value = "${auth.account.service-name}")
public interface AuthFeignClient {
    @RequestMapping(value = "${auth.account.user-info}",method = RequestMethod.GET)
    String getUserInfo(@RequestParam("accountId") String accountId);

    @RequestMapping(value = "${auth.account.group-info}",method = RequestMethod.GET)
    String getGroupInfo(@RequestParam("groupId") String groupId);
}