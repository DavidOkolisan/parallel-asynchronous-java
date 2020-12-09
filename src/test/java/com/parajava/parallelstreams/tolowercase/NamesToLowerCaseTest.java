package com.parajava.parallelstreams.tolowercase;

import com.parajava.util.DataSet;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.parajava.util.LoggerUtil.log;
import static org.junit.jupiter.api.Assertions.*;

class NamesToLowerCaseTest {

    NamesToLowerCase ntl = new NamesToLowerCase();

    @Test
    void transformNames() {
        List<String> names =  DataSet.namesList();
        ArrayList<String> temp = getLowerCaseNames(names);
        int count = names.size();

        List<String> transformedNames = ntl.transformNames(names);

        assertEquals(names.size(),transformedNames.size());
        log("Expected: " + temp);
        log("Actual result: " + transformedNames);
        assertEquals(temp,transformedNames);
    }

    private ArrayList<String> getLowerCaseNames(List<String> names) {
        ArrayList<String> temp = new ArrayList<>();
        for (int i=0;i< names.size();i++) {
            temp.add(names.get(i).toLowerCase());
        }

        return temp;
    }
}