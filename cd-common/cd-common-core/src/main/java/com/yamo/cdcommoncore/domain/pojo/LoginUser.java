package com.yamo.cdcommoncore.domain.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author mowanhui
 * @version 1.0
 * @date 2023/5/28 19:48
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class LoginUser implements Serializable {
    private static final long serialVersionUID = -3703561383599575836L;
    private String userId;

    private String policeId;

    private String userName;

    private String realName;

    private String idCard;

    private String phone;

    private String unitCode;

    private String allUnitCode;

    private String unitName;
    private List<String> roles;
    private List<String> permissions;
}
