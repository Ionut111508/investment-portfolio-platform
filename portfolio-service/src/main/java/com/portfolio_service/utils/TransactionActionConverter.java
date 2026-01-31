package com.portfolio_service.utils;

import com.portfolio_service.enums.TransactionAction;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TransactionActionConverter implements AttributeConverter<TransactionAction, String> {

    @Override
    public String convertToDatabaseColumn(TransactionAction attribute) {
        if (attribute == null) return null;
        return attribute.getLabel();
    }

    @Override
    public TransactionAction convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return TransactionAction.fromLabel(dbData);
    }
}

