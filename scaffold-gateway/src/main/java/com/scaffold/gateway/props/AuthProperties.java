package com.scaffold.gateway.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hui.zhang
 * @date 2022年04月16日 15:10
 */
@Data
@RefreshScope
@ConfigurationProperties("scaffold.security.skip")
public class AuthProperties {

    private List<String> skipUrl = new ArrayList<>();
}
