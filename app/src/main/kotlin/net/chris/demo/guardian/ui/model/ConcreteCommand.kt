package net.chris.demo.guardian.ui.model

class ConcreteCommand private constructor(private val receiver: CommandReceiver?) : Command() {

    override fun execute() {
        if (receiver == null) {
            return
        }
        receiver.handleCommand()
    }

    companion object {

        fun create(receiver: CommandReceiver?): Command {
            return ConcreteCommand(receiver)
        }
    }

}
