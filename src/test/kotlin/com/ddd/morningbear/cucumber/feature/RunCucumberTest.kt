package com.ddd.morningbear.cucumber.feature

import io.cucumber.junit.platform.engine.Constants
import org.junit.platform.suite.api.ConfigurationParameter
import org.junit.platform.suite.api.IncludeEngines
import org.junit.platform.suite.api.SelectClasspathResource
import org.junit.platform.suite.api.Suite
import org.junit.platform.suite.api.ConfigurationParameters

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("cucumber/feature")
@ConfigurationParameters(value = [
    ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME, value = "cucumber.feature"),
    ConfigurationParameter(key = Constants.PLUGIN_PROPERTY_NAME, value = "pretty"),
])
class RunCucumberTest {

}