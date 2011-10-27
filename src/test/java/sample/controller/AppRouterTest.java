package sample.controller;

import org.junit.Test;

import sample.test.AppRouterTestCase;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class AppRouterTest extends AppRouterTestCase {

    @Test
    public void test1() throws Exception {
        routingTest("/delete/abc",new Callback() {
            @Override
            public void test(String path, String query) throws Exception {
                assertThat(path,is(notNullValue()));
                assertThat(path,is("/delete"));

                assertThat(query,is(notNullValue()));
                assertThat(query,is("key=abc"));
            }
        });
    }

    @Test
    public void test2() throws Exception {
        routingTest("/uploads",new Callback() {
            @Override
            public void test(String path, String query) throws Exception {
                assertThat(path,is(notNullValue()));
                assertThat(path,is("/uploads/index"));

                assertThat(query,is(nullValue()));
            }
        });
    }

    @Test
    public void test3() throws Exception {
        routingTest("/uploads/thumb/abc",new Callback() {
            @Override
            public void test(String path, String query) throws Exception {
                assertThat(path,is(notNullValue()));
                assertThat(path,is("/uploads/thumb"));

                assertThat(query,is(notNullValue()));
                assertThat(query,is("key=abc"));
            }
        });
    }

    @Test
    public void test4() throws Exception {
        routingTest("/uploads/show/abc",new Callback() {
            @Override
            public void test(String path, String query) throws Exception {
                assertThat(path,is(notNullValue()));
                assertThat(path,is("/uploads/show"));

                assertThat(query,is(notNullValue()));
                assertThat(query,is("key=abc"));
            }
        });
    }
}