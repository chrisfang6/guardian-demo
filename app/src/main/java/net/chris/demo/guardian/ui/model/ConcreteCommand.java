package net.chris.demo.guardian.ui.model;

public class ConcreteCommand extends Command {

    private CommandReceiver receiver;

    private ConcreteCommand(CommandReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        if (receiver == null) {
            return;
        }
        receiver.handleCommand();
    }

    public static Command create(CommandReceiver receiver) {
        return new ConcreteCommand(receiver);
    }

}
