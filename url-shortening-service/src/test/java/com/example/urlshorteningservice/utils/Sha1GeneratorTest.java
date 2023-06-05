package com.example.urlshorteningservice.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class Sha1GeneratorTest {

    @Test
    @DisplayName("Test getHash with non-empty string")
    void testGetHash() {
        String input = "Hello, world!";
        String expectedHash = "943a702d06f34599aee1f8da8ef9f7296031d699";
        String actualHash = Sha1Generator.getHash(input);
        Assertions.assertEquals(expectedHash, actualHash);
    }

    @Test
    @DisplayName("Test getHash with empty string")
    void testGetHashWithEmptyString() {
        String input = "";
        Assertions.assertThrows(RuntimeException.class, () -> {
            Sha1Generator.getHash(input);
        });
    }
}

