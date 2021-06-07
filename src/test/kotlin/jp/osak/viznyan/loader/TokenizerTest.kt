package jp.osak.viznyan.loader

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.io.StringReader

internal class TokenizerTest {
    @Test
    fun readInt() {
        val tokenizer = Tokenizer(StringReader("12345 67890"))
        assertEquals(12345, tokenizer.readInt())
        assertDoesNotThrow { tokenizer.expect(' ') }
        assertEquals(67890, tokenizer.readInt())

        // After EOF, it returns null
        assertEquals(null, tokenizer.readInt())
    }

    @Test
    fun `readInt fails if the first char is not a digit`() {
        val tokenizer = Tokenizer(StringReader("abc"))
        assertThrows<ExpectationMismatchException> { tokenizer.readInt() }
    }

    @Test
    fun skipNonNewLineSpace() {
        val tokenizer = Tokenizer(StringReader("  123"))
        tokenizer.skipNonNewLineSpace()
        assertEquals(123, tokenizer.readInt())
    }

    @Test
    fun `skipNonNewLineSpace does not do anything if the first character is not a space`() {
        val tokenizer = Tokenizer(StringReader("1"))
        tokenizer.skipNonNewLineSpace()
        assertDoesNotThrow { tokenizer.expect('1') }
    }

    @Test
    fun `skipNonNewLineSpace stops after EOF`() {
        val tokenizer = Tokenizer(StringReader(""))
        tokenizer.skipNonNewLineSpace()
        assertEquals(null, tokenizer.readInt())
    }

    @Test
    fun expect() {
        val tokenizer = Tokenizer(StringReader("123"))
        assertDoesNotThrow { tokenizer.expect('1') }
        assertDoesNotThrow { tokenizer.expect('2') }
        assertDoesNotThrow { tokenizer.expect('3') }
    }

    @Test
    fun `expect failure`() {
        val tokenizer = Tokenizer(StringReader("123"))
        assertThrows<ExpectationMismatchException> { tokenizer.expect('2') }
    }

    @Test
    fun `expectNewLine skips LF`() {
        val tokenizer = Tokenizer(StringReader("\n1"))
        assertDoesNotThrow { tokenizer.expectNewLine() }
        assertDoesNotThrow { tokenizer.expect('1') }
    }

    @Test
    fun `expectNewLine skips CRLF`() {
        val tokenizer = Tokenizer(StringReader("\r\n1"))
        assertDoesNotThrow { tokenizer.expectNewLine() }
        assertDoesNotThrow { tokenizer.expect('1') }
    }

    @Test
    fun `expectNewLine skips CR`() {
        val tokenizer = Tokenizer(StringReader("\r1"))
        assertDoesNotThrow { tokenizer.expectNewLine() }
        assertDoesNotThrow { tokenizer.expect('1') }
    }

    @Test
    fun `expectNewLine fails on non-newline char`() {
        val tokenizer = Tokenizer(StringReader("1"))
        assertThrows<ExpectationMismatchException> { tokenizer.expectNewLine() }
    }
}