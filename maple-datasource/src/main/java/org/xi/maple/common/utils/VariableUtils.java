package org.xi.maple.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VariableUtils {
    private static Logger log = LoggerFactory.getLogger(VariableUtils.class);
    private static Pattern DATETIME_PATTERN = Pattern.compile("\\$\\{exec_time(\\s*[-+]\\s*\\d+[yMdHms]\\s*)*(\\|[^}]+?)?}");
    private static Pattern INCREMENT_PATTERN = Pattern.compile("[-+]\\d+[yMdHms]");
    private static String DEFAULT_DATE_FORMATTER_STR = "yyyy-MM-dd";
    private static String DEFAULT_DATETIME_FORMATTER_STR = "yyyy-MM-dd HH:mm:ss";
    private static DateTimeFormatter DEFAULT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATETIME_FORMATTER_STR);

    public static String replaceVariables(String content, Map<String, String> variables) {
        LocalDateTime localDateTime = LocalDateTime.now();
        if (variables.containsKey("execTimestamp")) {
            long execTimestamp = Long.parseLong(variables.get("execTimestamp"));
            localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(execTimestamp), ZoneId.of("Asia/Shanghai"));
        } else if (variables.containsKey("execDateTime")) {
            String execDateTime = variables.get("execDateTime");
            localDateTime = LocalDateTime.parse(execDateTime, DEFAULT_DATETIME_FORMATTER);
        }
        StringSubstitutor substitutor = new StringSubstitutor(variables);
        return substitutor.replace(replaceDateTimeExpression(content, localDateTime));
    }

    public static String replaceDateTimeExpression(String content, LocalDateTime datetime) {
        Matcher matcher = DATETIME_PATTERN.matcher(content);
        StringBuffer sb = new StringBuffer(content.length());
        while (matcher.find()) {
            String variable = matcher.group();
            String result = variable;
            try {
                result = getDateTimeFormatResult(datetime, variable);
            } catch (Throwable t) {
                log.info("replace time failed, {}", t.getMessage());
            }
            matcher.appendReplacement(sb, result.replace("$", "\\$"));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static String getDateTimeFormatResult(LocalDateTime time, String variable) {
        int splitIndex = variable.indexOf("|");
        String increment;
        String format;
        if (splitIndex == -1) {
            increment = variable.substring("${exec_time".length(), variable.length() - 1).replaceAll("\\s", "");
            format = DEFAULT_DATE_FORMATTER_STR;
        } else {
            increment = variable.substring("${exec_time".length(), splitIndex).replaceAll("\\s", "");
            format = variable.substring(splitIndex + 1, variable.length() - 1).trim();
        }

        if (StringUtils.isBlank(increment)) {
            return time.format(DateTimeFormatter.ofPattern(format));
        }

        LocalDateTime resultDateTime = time;
        Matcher matcher = INCREMENT_PATTERN.matcher(increment);
        while (matcher.find()) {
            String group = matcher.group();
            int incrementNum = Integer.parseInt(group.substring(0, group.length() - 1));
            String incrementUnit = group.substring(group.length() - 1);
            resultDateTime = getDateTime(resultDateTime, incrementNum, incrementUnit);
        }
        return DateTimeFormatter.ofPattern(format).format(resultDateTime);
    }

    private static LocalDateTime getDateTime(LocalDateTime time, int incrementNum, String incrementUnit) {
        switch (incrementUnit) {
            case "y":
                return time.plusYears(incrementNum);
            case "M":
                return time.plusMonths(incrementNum);
            case "d":
                return time.plusDays(incrementNum);
            case "H":
                return time.plusHours(incrementNum);
            case "m":
                return time.plusMinutes(incrementNum);
            case "s":
                return time.plusSeconds(incrementNum);
            default:
        }
        return time.plusDays(incrementNum);
    }
}
