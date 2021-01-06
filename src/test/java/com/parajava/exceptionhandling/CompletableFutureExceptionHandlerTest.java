package com.parajava.exceptionhandling;

import com.parajava.service.HelloWorldService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompletableFutureExceptionHandlerTest {

    @Mock
    HelloWorldService hws = mock(HelloWorldService.class);

    @InjectMocks
    CompletableFutureExceptionHandler cfeh;

    @Test
    void exceptionHandleHelloTest() {
        when(hws.hello()).thenThrow(new RuntimeException("Exception occured when helloworld called."));
        when(hws.world()).thenCallRealMethod();

        String result = cfeh.helloWorldHandle();
        assertEquals(" WORLD! + THIRD ASYNC CALL", result);
    }

    @Test
    void exceptionHandleHelloWorldTest() {
        when(hws.hello()).thenThrow(new RuntimeException("Exception occured when hello() called."));
        when(hws.world()).thenThrow(new RuntimeException("Exception occured when world() called."));

        String result = cfeh.helloWorldHandle();
        assertEquals(" + THIRD ASYNC CALL", result);
    }


    @Test
    void asyncVersionWith3CallsTest() {
        when(hws.hello()).thenCallRealMethod();
        when(hws.world()).thenCallRealMethod();

        String result = cfeh.helloWorldHandle();
        assertEquals("HELLO WORLD! + THIRD ASYNC CALL",result);
    }

    @Test
    void exceptionallyTest() {
        when(hws.hello()).thenCallRealMethod();
        when(hws.world()).thenCallRealMethod();

        String result = cfeh.helloWorldExceptionally();
        assertEquals("HELLO WORLD! + THIRD ASYNC CALL",result);
    }

    @Test
    void exceptionallyThrowExceptionTest() {
        when(hws.hello()).thenThrow(new RuntimeException("Exception occured when hello() called."));
        when(hws.world()).thenThrow(new RuntimeException("Exception occured when world() called."));

        String result = cfeh.helloWorldExceptionally();
        assertEquals(" + THIRD ASYNC CALL", result);
    }

    @Test
    void whenCompleteTest() {
        when(hws.hello()).thenCallRealMethod();
        when(hws.world()).thenCallRealMethod();

        String result = cfeh.helloWorldWhenComplete();
        assertEquals("HELLO WORLD! + THIRD ASYNC CALL",result);
    }

    @Test
    void whenCompleteThrowExceptionTest() {
        when(hws.hello()).thenThrow(new RuntimeException("Exception occured when hello() called."));
        when(hws.world()).thenThrow(new RuntimeException("Exception occured when world() called."));

        String result = cfeh.helloWorldWhenComplete();
        assertEquals(" + THIRD ASYNC CALL", result);
    }


}