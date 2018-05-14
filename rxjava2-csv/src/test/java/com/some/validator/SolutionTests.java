package com.some.validator;

import com.google.common.collect.Lists;
//import com.some.model.RecordTuple;
import com.google.common.primitives.Ints;
import com.opencsv.CSVReader;
import com.some.model.Person;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.flowables.GroupedFlowable;
import io.reactivex.functions.Consumer;
import io.reactivex.observables.GroupedObservable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import io.reactivex.subscribers.TestSubscriber;
import javafx.util.Pair;
import org.junit.Test;
import org.reactivestreams.Subscriber;

import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SolutionTests {

    private static String betweenWordsPattern = "\\s(?=[A-Z])|(?<=\\s)\\s+";
    private static String csvPath = "src/main/resources/input/pii.csv";
    private static String middleInitialPattern = "\\s(?=[A-Z]).";


    private static String convertObjectArrayToString(String[] arr, String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (Object obj : arr)
            sb.append(obj.toString()).append(delimiter);
        return sb.substring(0, sb.length() - 1);

    }

    @Test
    public void testSplitFields() throws Exception {
        String[] line3 = {"Jamie Stevenson", " yellow", " 84880", " 028 164 6574"};
        List<String> newList = Lists.newArrayList();
        for (String s : line3) {
            newList.addAll(Arrays.asList(s.split(betweenWordsPattern)));
        }
         assert(newList.size()==5);

    }

    @Test
    public void testMiddleInitial() throws Exception {
        String[] line1 = {"Sam T.", "Washington", "85360", "353 791 6380", "purple"};
        Pattern p = Pattern.compile(middleInitialPattern);

        for (String s : line1) {
            Matcher m1 = p.matcher(s);
            if (m1.find())
                assert(s.equals("Sam T."));
        }
    }

    @Test
    public void testGroupBy() throws Exception {

        String[] line1 = {"Sam T.", "Washington", "85360", "353 791 6380", "purple"};
        String[] line2 = {"Cameron", " Kathy", " (613)-658-9293", " red", " 143123121"};
        String[] line3 = {"Jamie Stevenson", " yellow", " 84880", " 028 164 6574"};
        String[] line4 = {"asdfawfsdfsdfdsjh"};

        List<String[]> lines = Arrays.asList(line1, line2, line3, line4);
        Flowable<String[]> csvLines = Flowable.fromIterable(lines);
        Flowable<Integer> lineNumbers = Flowable.range(0, Ints.checkedCast(lines.size()));

    }

}
