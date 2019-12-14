package de.kaiserpfalzedv.base.api.spec;

import org.junit.jupiter.api.Test;

public class EmptySpecTest {
    @Test
    public void shouldCreateAnEmptySpec() {
        EmptySpec<EmptySpec> service = new EmptySpec<>();

        assert service != null;
    }
}
