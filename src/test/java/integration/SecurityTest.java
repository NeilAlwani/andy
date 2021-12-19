package integration;

import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.result.Result;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class SecurityTest extends IntegrationTestBase {

    @ParameterizedTest
    @CsvSource({
            "WriteResultsXml,name=results.xml actions=write",
            "SystemExit,name=exitVM.",
            "SetProperty,test actions=write",
            "RuntimeExec,actions=execute",
            "ReadSource,ExploitTest.java actions=read",
            "ReadClass,ExploitTest$1.class actions=read"
    })
    void securityTest(String exploitFile, String expectedMessage) {
        // Provide working directory path to user code for testing purposes
        System.setProperty("andy.securitytest.workdir", workDir.getAbsolutePath());

        Result result = run(Action.TESTS, "EmptyLibrary", "securitytests/" + exploitFile, "EmptyConfiguration");

        assertThat(result.getTests().getFailures().get(0).getMessage())
                .startsWith(SecurityException.class.getName())
                .contains("Operation not permitted")
                .contains(expectedMessage);
    }

    @Test
    void staticBlock() {
        Result result = run(Action.FULL_WITHOUT_HINTS, "EmptyLibrary", "securitytests/StaticBlock", "EmptyConfiguration");

        assertThat(result.getTests().getFailures().get(0).getMessage())
                .startsWith(ExceptionInInitializerError.class.getName());
    }

    @ParameterizedTest
    @CsvSource({
            "OtherPackageName,package name of your solution",
            "InstantiateConfiguration,Accessing the task configuration",
            "UseReflection,Using reflection"
    })
    void failingSecurityCheck(String exploitFile, String expectedMessage) {
        Result result = run(Action.FULL_WITHOUT_HINTS, "EmptyLibrary", "securitytests/"+exploitFile, "EmptyConfiguration");

        assertThat(result.getCompilation().successful()).isFalse();
        assertThat(result.getCompilation().getErrors())
                .hasSize(1)
                .allMatch(err -> err.getMessage().contains(expectedMessage));
    }


}