package com.yamo.cdcommonapi.feign.auth;

import cn.hutool.json.JSONObject;
import com.yamo.cdcommoncore.config.FeignHttpsConfig;
import com.yamo.cdcommoncore.domain.dto.ZeroTrustAppSignDTO;
import com.yamo.cdcommoncore.domain.dto.ZeroTrustUserSignDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 零信任常用接口
 */
@FeignClient(value = "ZeroTrustFeignClient", configuration = FeignHttpsConfig.class,url = "${zero-trust.service.base-url}")
public interface ZeroTrustFeignClient {
    @PostMapping(value = "${zero-trust.service.refresh-app-token}",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    JSONObject getRefreshAppToken(@RequestBody ZeroTrustAppSignDTO zeroTrustAppSignDTO);

    @PostMapping(value = "${zero-trust.service.offline-app-token}",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    JSONObject offLineAppToken(@RequestBody ZeroTrustAppSignDTO zeroTrustAppSignDTO);

    @PostMapping(value = "${zero-trust.service.offline-user-token}",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    JSONObject offLineUserToken(@RequestBody ZeroTrustUserSignDTO zeroTrustUserSignDTO);

    @PostMapping(value = "${zero-trust.service.validate-app-token}",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    JSONObject validateAppToken(@RequestParam("appToken") String appToken);
}
