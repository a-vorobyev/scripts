import ansible.JsonFactory
import ansible.cli.Argument
import ansible.cli.Operation
import ansible.model.Host
import ansible.model.Inventory
import groovy.json.JsonOutput
import groovy.json.JsonSlurper


Map<Argument, String> arguments = Argument.collect(args as List)

Operation operation = Operation.of(arguments.keySet())

switch (operation) {

    case Operation.LIST:

        String fileName = arguments[Argument.FILE]

        Map<String, ?> inventoryData = new JsonSlurper().parse(new File(fileName))

        Inventory inventory = new JsonFactory(inventoryData).createInventory()

        inventoryData['_meta'] = [hostvars: inventory.allHostsVars]

        println JsonOutput.prettyPrint(JsonOutput.toJson(inventoryData))

        break

    case Operation.HOST:

        String fileName = arguments[Argument.FILE]

        Inventory inventory = JsonFactory.create(new File(fileName))

        Host host = inventory.hosts.find { it.name == arguments[Argument.HOST] }

        assert host, "there is no host ${arguments[Argument.HOST]} in inventory"

        Map<String, ?> hostVars = inventory.getHostVars(host)

        println JsonOutput.prettyPrint(JsonOutput.toJson(hostVars))

        break

    default:
        throw new UnsupportedOperationException("$operation not supported yet")

}

