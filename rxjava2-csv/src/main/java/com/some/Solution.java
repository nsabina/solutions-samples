package com.some;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.CSVReader;
import com.some.model.JsonOutput;
import com.some.model.Person;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import javafx.util.Pair;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

    public static void main(String[] args) throws IOException {
        List<Long> errors = Lists.newArrayList();
        List<Person> entries = Lists.newArrayList();

        //Map<Long,Object> entriesMap = new HashMap<>();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        CSVReader reader = new CSVReader(new FileReader(args[0]));

        Flowable<Long> naturalNumbers =
                Flowable.generate(() -> 0L, (state, emitter) -> {
                    emitter.onNext(state);
                    return state + 1;
                });


        Flowable
                .zip(
                        naturalNumbers,
                        Flowable.fromIterable(reader)
                                .map(e -> parseLineToObjectFunction(e)),
                        (lineNo, wordsArray) -> new Pair<>(lineNo, wordsArray)
                )
                .parallel(100)
                .runOn(Schedulers.io())
                .sequential()
                //.subscribe(pair -> entriesMap.put(pair.getKey(), pair.getValue()));
                .subscribe(event -> {
                    if (event.getValue() instanceof Error) {
                        errors.add(event.getKey());
                    } else {
                        entries.add(Person.class.cast(event.getValue()));
                    }
                });


        Collections.sort(errors);

        try (Writer writer = new FileWriter(args[1])) {
            gson.toJson(new JsonOutput(entries, errors), writer);
        }

    }

    private static Object parseLineToObjectFunction ( String[] personRecordFields) {
        int setFieldsCount = 0;
        String middleInitialPattern = "\\s(?=[A-Z]\\.)";
        Pattern p = Pattern.compile(middleInitialPattern);
        String firstname = null;
        String lastname= null;
        String color= null;
        String phonenumber= null;
        String zipcode = null;
        for (String field : personRecordFields) {
            field = field.trim();
            Matcher m = p.matcher(field);
            boolean containsMiddleInitial = m.find();
            String betweenWordsPattern = "\\s(?=[A-Z])|(?<=\\s)\\s+";
            String[] tokens = field.split(betweenWordsPattern);
            if (tokens.length == 2
                    && !containsMiddleInitial) {
                firstname = tokens[0];
                lastname =tokens[1];
                setFieldsCount = setFieldsCount + 2;
            }
            if (firstname == null) {
                firstname = field;
                setFieldsCount++;
            }
            if (lastname == null) {
                lastname = field;
                setFieldsCount++;
            }
            String phoneNumberPattern = "(?:\\d{3} ){2}\\d{4}|\\(\\d{3}\\)-\\d{3}-?\\d{4}";
            if (phonenumber == null && field.matches(phoneNumberPattern)) {
                phonenumber =field;
                setFieldsCount++;
            }
            String zipPattern = "\\d{5}";
            if (zipcode == null && field.matches(zipPattern)) {
                zipcode =field;
                setFieldsCount++;
            }
            if (color == null) {
                color = field;
                setFieldsCount++;
            }
        }
        if (setFieldsCount == 5) {
            return new Person(
                    firstname,
                    lastname,
                    color,
                    phonenumber,
                    zipcode);
        } else return new Error();
    }

}
