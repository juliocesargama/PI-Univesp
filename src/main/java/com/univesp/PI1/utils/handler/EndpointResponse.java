package com.univesp.PI1.utils.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EndpointResponse implements Serializable {

    private String message;
}
