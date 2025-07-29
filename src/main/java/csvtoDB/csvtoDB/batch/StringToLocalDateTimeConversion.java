package csvtoDB.csvtoDB.batch;


import org.springframework.core.convert.converter.Converter;
import org.springframework.format.support.DefaultFormattingConversionService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StringToLocalDateTimeConversion extends DefaultFormattingConversionService {
    public StringToLocalDateTimeConversion() {
        addConverter(String.class, LocalDateTime.class, new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(String source) {
                return LocalDateTime.parse(source.trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
        });
    }
}
