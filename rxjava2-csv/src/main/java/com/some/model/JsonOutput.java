package com.some.model;

import com.google.common.collect.Lists;

import java.util.List;

public class JsonOutput {
    private List<Person> entries = Lists.newArrayList();
    private List<Long> errors = Lists.newArrayList();

    public JsonOutput(List<Person> entries, List<Long> errors) {
        this.entries = entries;
        this.errors = errors;
    }

    public List<Person> getEntries() {
        return entries;
    }

    public void setEntries(List<Person> entries) {
        this.entries = entries;
    }

    public List<Long> getErrors() {
        return errors;
    }

    public void setErrors(List<Long> errors) {
        this.errors = errors;
    }
}
