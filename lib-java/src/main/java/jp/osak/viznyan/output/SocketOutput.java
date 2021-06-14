package jp.osak.viznyan.output;

import jp.osak.viznyan.command.Command;
import jp.osak.viznyan.state.State;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collection;

public class SocketOutput implements Output, Closeable {
    private final Socket socket;
    private final OutputStream outputStream;
    private final CompProgSerializer compProgSerializer = new CompProgSerializer();
    private final CommandSerializer commandSerializer = new CommandSerializer();

    public SocketOutput(int port) throws IOException {
        socket = new Socket("localhost", port);
        outputStream = socket.getOutputStream();
    }

    public void write(State state) {
        compProgSerializer.serialize(outputStream, state);
    }

    @Override
    public void write(Collection<? extends Command> commands) {
        commandSerializer.serialize(outputStream, commands);
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }
}
