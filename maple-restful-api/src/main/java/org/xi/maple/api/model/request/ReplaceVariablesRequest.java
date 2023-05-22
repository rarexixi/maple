package org.xi.maple.api.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class ReplaceVariablesRequest implements Serializable {
    String content;
    Map<String, String> variables;
}
