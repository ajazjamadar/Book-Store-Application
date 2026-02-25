package com.qburst.training.bookstoreapplication.Factory;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

/**
 * Factory Pattern: Creates payment processor instances based on payment type.
 * 
 * Spring auto-discovers all PaymentProcessor implementations and maps them by type.
 * To add new payment method: create @Component implementing PaymentProcessor.
 */
@Component
public class PaymentProcessorFactory {
    
    private final Map<String, PaymentProcessor> processors;
    
    public PaymentProcessorFactory(List<PaymentProcessor> processorList) {
        this.processors = processorList.stream()
            .collect(Collectors.toMap(
                PaymentProcessor::getType,
                Function.identity()
            ));
    }
    
    public PaymentProcessor getProcessor(String type) {
        PaymentProcessor processor = processors.get(type.toLowerCase());
        if (processor == null) {
            throw new IllegalArgumentException(
                "Unknown payment type: " + type + ". Available: " + processors.keySet());
        }
        return processor;
    }
    
    public java.util.Set<String> getAvailableTypes() {
        return processors.keySet();
    }
}
