package org.xi.maple.rest.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class ReplaceVariablesReq implements Serializable {
    String content;
    Map<String, String> variables;
}
