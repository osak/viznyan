package jp.osak.viznyan.output;

import jp.osak.viznyan.command.*;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collection;

public class CommandSerializer {
    public void serialize(OutputStream outputStream, Collection<? extends Command> commands) {
        final PrintWriter writer = new PrintWriter(outputStream);
        writer.println(commands.size());
        for (Command command : commands) {
            if (command instanceof AddCircle) {
                final AddCircle addCircle = (AddCircle) command;
                writer.printf("add_circle %d %d %d %d\n", addCircle.getId(), addCircle.getX(), addCircle.getY(), addCircle.getRadius());
            } else if (command instanceof MoveCircle) {
                final MoveCircle moveCircle = (MoveCircle) command;
                writer.printf("move_circle %d %d %d\n", moveCircle.getId(), moveCircle.getX(), moveCircle.getY());
            } else if (command instanceof AddRect) {
                final AddRect addRect = (AddRect) command;
                writer.printf("add_rect %d %d %d %d %d\n", addRect.getId(), addRect.getX1(), addRect.getY1(), addRect.getX2(), addRect.getY2());
            } else if (command instanceof AddLine) {
                final AddLine addLine = (AddLine) command;
                writer.printf("add_line %d %d %d %d %d\n", addLine.getId(), addLine.getX1(), addLine.getY1(), addLine.getX2(), addLine.getY2());
            } else if (command instanceof AddText) {
                final AddText addText = (AddText) command;
                writer.printf("add_text %d %d %d %s\n", addText.getId(), addText.getX(), addText.getY(), addText.getText());
            } else {
                throw new IllegalArgumentException("Unsupported command type: " + command.getClass());
            }
        }
        writer.flush();
    }
}
