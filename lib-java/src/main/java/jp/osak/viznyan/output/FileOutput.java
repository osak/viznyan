package jp.osak.viznyan.output;

import jp.osak.viznyan.command.Command;
import jp.osak.viznyan.state.State;

import java.io.*;
import java.util.Collection;

public class FileOutput implements Output, Closeable {
    private final File file;
    private final BufferedOutputStream outputStream;
    private final CompProgSerializer compProgSerializer = new CompProgSerializer();
    private final CommandSerializer commandSerializer = new CommandSerializer();

    public FileOutput(File file) throws IOException {
        this.file = file;
        this.outputStream = new BufferedOutputStream(new FileOutputStream(file));
    }

    public void write(State state) throws IOException {
        compProgSerializer.serialize(outputStream, state);
    }

    @Override
    public void write(Collection<? extends Command> commands) throws IOException {
        commandSerializer.serialize(outputStream, commands);
    }

    @Override
    public void close() throws IOException {
        outputStream.close();
    }
}
