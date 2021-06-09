package jp.osak.viznyan.loader

import java.io.Closeable
import java.io.Reader

class Tokenizer(private val reader: Reader) : Closeable {
    private var unreadBuffer: Char? = null

    /**
     * Read an integer value.
     *
     * @return The integer value, or `null` if the underlying reader has already reached EOF.
     */
    fun readInt(): Int? {
        // Check if the reader is not exhausted yet.
        // If it's already exhausted, return null to indicate EOF.
        val firstChar = readChar() ?: return null
        unread(firstChar)

        // Ensure this is a valid beginning of a number.
        if (!firstChar.isDigit()) {
            throw ExpectationMismatchException("Expected a digit, but got '$firstChar'")
        }

        var value = 0
        while (true) {
            val char = readChar() ?: break

            if (char.isDigit()) {
                value *= 10
                value += char - '0'
            } else {
                unread(char)
                break
            }
        }
        return value
    }

    /**
     * Read a string until newline character.
     *
     * @return The string until newline character, or `null` if the underlying reader has already reached EOF.
     */
    fun readUntilNewLine(): String? {
        val firstChar = readChar() ?: return null
        unread(firstChar)

        val buffer = StringBuffer()
        while (true) {
            val char = readChar() ?: break
            if (char == '\n' || char == '\r') {
                unread(char)
                break
            }
            buffer.append(char)
        }
        return buffer.toString()
    }

    fun skipNonNewLineSpace() {
        while (true) {
            val char = readChar()
            if (char == ' ' || char == '\t') {
                continue
            } else {
                if (char != null) {
                    unread(char)
                }
                break
            }
        }
    }

    fun expect(c: Char) {
        val char = readChar()
        if (char != c) {
            throw ExpectationMismatchException("Expected '$c' but read '$char'")
        }
    }

    fun expectNewLine() {
        val char = readChar()
        if (char == '\r') {
            // Test if it's CRLF
            val maybeLF = readChar()
            if (maybeLF == '\n') {
                // ok: CRLF
                return
            } else {
                // It was not CRLF, but still it's a valid CR-only newline
                // Unread the last char for later
                if (maybeLF != null) {
                    unread(maybeLF)
                }
                return
            }
        } else if (char == '\n') {
            // ok: LF-only newline
            return
        }
        throw ExpectationMismatchException("Expected a newline but read '$char'")
    }

    override fun close() {
        reader.close()
    }

    private fun readChar(): Char? {
        if (unreadBuffer != null) {
            return unreadBuffer.also { unreadBuffer = null }
        }

        return reader.read().toCharOrEof()
    }

    private fun unread(c: Char) {
        require(unreadBuffer == null) { "Unread buffer is must be empty" }
        unreadBuffer = c
    }

    private fun Int.toCharOrEof(): Char? {
        return when (this) {
            -1 -> null
            else -> toChar()
        }
    }
}

class ExpectationMismatchException(message: String) : RuntimeException(message)