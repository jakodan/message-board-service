package com.example.messageboardservice.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class ValidationUtils {

  public static void validateNotEmptyOrNull(String val, String parameterName) {
    if (StringUtils.isEmpty(val)) {
      throw new IllegalArgumentException(String.format("Illegal value=% for parameter=%", val, parameterName));
    }
  }
}
