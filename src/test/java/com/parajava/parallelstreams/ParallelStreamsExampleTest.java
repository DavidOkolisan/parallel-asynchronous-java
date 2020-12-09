package com.parajava.parallelstreams;

import com.parajava.util.DataSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.xml.crypto.Data;

import java.util.List;

import static com.parajava.util.CommonUtil.*;
import static org.junit.jupiter.api.Assertions.*;

class ParallelStreamsExampleTest {

    ParallelStreamsExample pse = new ParallelStreamsExample();

    @Test
    void stringTransform() {
        List<String> input = DataSet.namesList();

        startTimer();
        List<String> result = pse.stringTransform(input);
        returnTime();

        assertEquals(4, result.size());
        result.forEach(name -> {
            assertTrue(name.contains("-"));
        });
    }


    /**
     * test both sequential and parallel
     */
    @ParameterizedTest
    @ValueSource(booleans = {true,false})
    void stringTransformByTweakingStreams(boolean isParallel) {

        List<String> input = DataSet.namesList();

        startTimer();
        List<String> result = pse.stringTransformByTweakingStreams(input, isParallel);
        returnTime();

        // reset stopwatch
        stopWatchReset();

        assertEquals(4, result.size());
        result.forEach(name -> {
            assertTrue(name.contains("-"));
        });
    }
}