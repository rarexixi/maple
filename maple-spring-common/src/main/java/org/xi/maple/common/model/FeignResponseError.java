package org.xi.maple.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeignResponseError {

    private ResponseError error;

    @JsonIgnore
    private FeignException exception;
}
