package com.almod.camel;

public enum TypeDoc {
    TXT("txt"),
    XML("xml"),
    UNDEFINED("undefined");

    private String value;

    TypeDoc(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }
}
