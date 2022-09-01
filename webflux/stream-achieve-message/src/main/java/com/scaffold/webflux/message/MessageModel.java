package com.scaffold.webflux.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hui.zhang
 * @date 2022年09月01日 20:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageModel {

    private String reqBody;
    private String respBody;

}
