package jp.osak.viznyan.output;

import jp.osak.viznyan.state.State;

import java.io.*;

public class FileOutput implements Closeable {
    private final File file;
    private final BufferedOutputStream outputStream;
    private final CompProgSerializer compProgSerializer = new CompProgSerializer();

    public FileOutput(File file) throws IOException {
        this.file = file;
        this.outputStream = new BufferedOutputStream(new FileOutputStream(file));
    }

    public void write(State state) throws IOException {
        compProgSerializer.serialize(outputStream, state);
    }

    @Override
    public void close() throws IOException {
        outputStream.close();
    }
}
