package com.parajava.parallelstreams.tolowercase;

import com.parajava.util.DataSet;

import java.util.List;
import java.util.stream.Collectors;

import static com.parajava.util.CommonUtil.delay;
import static com.parajava.util.CommonUtil.startTimer;
import static com.parajava.util.LoggerUtil.log;

public class NamesToLowerCase {

    public static void main(String[] args) {
        List<String> nameList = DataSet.namesList();
        NamesToLowerCase namesToLowerCase = new NamesToLowerCase();
        startTimer();
        List<String> results = namesToLowerCase.transformNames(nameList);
        log("Results : " + results);
    }

    public List<String> transformNames(List<String> nameList) {
        return nameList
                .parallelStream()
                .map(this::toLowerCase)
                .collect(Collectors.toList());
    }

    private String toLowerCase(String name) {
        delay(500);
        return name.toLowerCase();
    }
}
