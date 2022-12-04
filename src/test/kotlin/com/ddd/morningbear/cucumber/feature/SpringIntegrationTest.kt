package com.ddd.morningbear.cucumber.feature

import io.cucumber.spring.CucumberContextConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.graphql.test.tester.GraphQlTester
import org.springframework.test.context.ActiveProfiles

/**
 * @author yoonho
 * @since 2022.12.04
 */
@SpringBootTest
@CucumberContextConfiguration
@AutoConfigureMockMvc
@AutoConfigureGraphQlTester
@ActiveProfiles("test")
class SpringIntegrationTest {

}