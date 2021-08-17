package nl.tudelft.cse1110.grader.execution.step;

import org.junit.jupiter.api.Test;

import static nl.tudelft.cse1110.grader.execution.step.GraderIntegrationTestAssertions.parameterizedTestFailing;
import static nl.tudelft.cse1110.grader.execution.step.GraderIntegrationTestHelper.justTests;
import static nl.tudelft.cse1110.grader.execution.step.GraderIntegrationTestAssertions.propertyTestFailing;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GraderJQWikTest extends GraderIntegrationTestBase {


    @Test
    void testSimplePropertyTest() {
        String result = run(justTests(), "ArrayUtilsIndexOfLibrary", "ArrayUtilsIndexOfSimpleJqwikError");
        assertThat(result)
                .has(propertyTestFailing("testNoElementInWholeArray"));
    }


    @Test
    void testMultiplePropertyTestsFailing() {
        String result = run(justTests(), "ArrayUtilsIndexOfLibrary", "ArrayUtilsIndexOfMultipleJqwikErrors");
        assertThat(result)
                .has(propertyTestFailing("testNoElementInWholeArray"))
                .has(propertyTestFailing("testValueInArrayUniqueElements"));
    }


    @Test
    void testMultiplePropertyWithParameterizedTests() {
        String result = run(justTests(), "ArrayUtilsIndexOfLibrary", "ArrayUtilsIndexOfJqwikWithParameterized");
        assertThat(result)
                .has(propertyTestFailing("testNoElementInWholeArray"))
                .has(propertyTestFailing("testValueInArrayUniqueElements"))
                .has(parameterizedTestFailing("test", 6));
    }


    @Test
    void testMessageOtherThanAssertionError() {
        String result = run(justTests(), "NumberUtilsAddPositiveLibrary", "NumberUtilsAddPositiveJqwikException");
        assertThat(result)
                .has(propertyTestFailing("testAddition"));
    }


}