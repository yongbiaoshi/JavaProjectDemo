package com.tsingda.smd.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration
@TestPropertySource("/test.properties")
@RunWith(SpringJUnit4ClassRunner.class)
public class MyIntegrationTests {

}
