package com.parajava.parallelstreams;
 import com.parajava.util.DataSet;

import java.util.List;
 import java.util.stream.Collectors;
 import java.util.stream.Stream;

 import static com.parajava.util.CommonUtil.*;
 import static com.parajava.util.LoggerUtil.log;

public class ParallelStreamsExample {


    public List<String> stringTransform(List<String> namesList){
        /**
         * Beside stream and parallelStream api behaviour can be
         * bypassed by using sequential() or parallel() methods from
         * stream API.
         */

        return namesList
                //.stream()
                .parallelStream()
                .map(this::addNameLengthTransform)
                .collect(Collectors.toList());
    }

    public List<String> stringTransformByTweakingStreams(List<String> namesList, boolean useParallel){

        Stream<String> names = namesList.stream();

        if(useParallel) names.parallel();

        return names
                .map(this::addNameLengthTransform)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {

        List<String> namesList = DataSet.namesList();
        ParallelStreamsExample parallelStreamsExample = new ParallelStreamsExample();
        startTimer();
        List<String> resultList = parallelStreamsExample.stringTransform(namesList);
        log("Result list : " + resultList);
        returnTime();

    }

    private  String addNameLengthTransform(String name) {
        delay(500);
        return name.length() + " - " + name ;
    }
}
