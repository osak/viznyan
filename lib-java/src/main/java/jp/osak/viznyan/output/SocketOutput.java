package jp.osak.viznyan.output;

import jp.osak.viznyan.state.State;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class SocketOutput implements Closeable {
    private final Socket socket;
    private final OutputStream outputStream;
    private final CompProgSerializer compProgSerializer = new CompProgSerializer();

    public SocketOutput(int port) throws IOException {
        socket = new Socket("localhost", port);
        outputStream = socket.getOutputStream();
    }

    public void write(State state) {
        compProgSerializer.serialize(outputStream, state);
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }
}
