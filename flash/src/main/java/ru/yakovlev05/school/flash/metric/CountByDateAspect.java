package ru.yakovlev05.school.flash.metric;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@RequiredArgsConstructor
@Aspect
@Component
public class CountByDateAspect {

    private final MeterRegistry meterRegistry;

    @Before("@annotation(countByDate)")
    public void countByDate(final CountByDate countByDate) {
        System.out.println("---");
        meterRegistry.counter("app." + countByDate.name(), "date", LocalDate.now().toString())
                .increment();
    }
}
