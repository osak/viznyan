package jp.osak.viznyan.output;

import jp.osak.viznyan.command.Command;

import java.io.IOException;
import java.util.Collection;

public interface Output {
    void write(Collection<? extends Command> commands) throws IOException;
}
