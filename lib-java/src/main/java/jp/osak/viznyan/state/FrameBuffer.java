package jp.osak.viznyan.state;

import jp.osak.viznyan.command.Command;
import jp.osak.viznyan.output.Output;

import java.io.IOException;
import java.util.ArrayList;

public class FrameBuffer {
    private final Output output;
    private final ArrayList<Command> commands = new ArrayList<>();

    public FrameBuffer(Output output) {
        this.output = output;
    }

    public void add(Command command) {
        commands.add(command);
    }

    public void endFrame() throws IOException {
        output.write(commands);
        commands.clear();
    }
}
