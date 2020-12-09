# Java parallel-asyncronous workshop
Repo used for hand on experience for parallel and async programming in Java.

For Intellij IDE Lombok plugin is required as well as well as enabling anotation processing.

While ProductService and ProductServiceUsingExecutor are more task oriented forkjoing and parallel 
streams are more data oriented.

ParallelStreams are not always the best choice since in some cases (ie for LinkedLists or Sets - Collection Unordered)
it could take more time for processing, the same applies in case of present (Un)Boxing

Modifying default parallelism (nr of cores - 1):
System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "100")
-Djava.util.concurrent.ForkJoinPool.common.parallelism=100

When to use parallel streams:
- When split and combine operations are faster then with stream API (not use it ie for LinkedList)
- computation taking a lot of time
- lot of data (dont use it when you have small amount of data)
- more cores in machine
- ALWAYS COMPARE PERFORMANCE WITH STREAM API
- Don't use it when you have many (un)boxing operations
- Don't use it when you have stream operators such as iterate() and limit()




