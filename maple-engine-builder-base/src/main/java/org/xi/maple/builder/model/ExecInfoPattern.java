package org.xi.maple.builder.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.regex.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@With
public class ExecInfoPattern {
    String key;
    Pattern pattern;
}
