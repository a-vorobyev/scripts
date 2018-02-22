import ansible.IniFactory
import ansible.cli.Argument
import ansible.cli.Operation
import ansible.model.Inventory


Map<Argument, String> arguments = Argument.collect(args as List)

Operation operation = Operation.of(arguments.keySet())

switch (operation) {

    case Operation.LIST:

        String fileName = arguments[Argument.FILE]

        Inventory inventory = IniFactory.create(new File(fileName))

        println inventory.render()

        break

    case Operation.HOST:

        String fileName = arguments[Argument.FILE]

        Inventory inventory = IniFactory.create(new File(fileName))

        println inventory.renderHost(arguments[Argument.HOST])

        break

    default:
        throw new UnsupportedOperationException("$operation not supported yet")

}

