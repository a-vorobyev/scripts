import ansible.IniFactory
import ansible.cli.Argument
import ansible.cli.Operation
import ansible.model.Inventory
import provider.HostDataProvider
import provider.ProviderFactory

def GCP_HOST_KEY = 'gcp_name'
def GCP_DATA_KEY = 'gcp_data'
def APP_NAME = 'GCP-JAVA-CLIENT'

def processInventory = { Inventory inventory, String gcpProject, String gcpZone ->
    def gcpHosts = inventory.hosts.findAll { it.vars.containsKey(GCP_HOST_KEY) && it.vars[GCP_HOST_KEY] }
    if (gcpHosts) {
        HostDataProvider hostDataProvider = ProviderFactory.createGcpHostDataProvider(APP_NAME, gcpProject, gcpZone)
        gcpHosts.each {
            String key = it.vars[GCP_HOST_KEY]
            def gcpData = hostDataProvider.data[key]
            if (gcpData) {
                it.vars[GCP_DATA_KEY] = gcpData
                def natIps = gcpData?.networkInterfaces?.accessConfigs?.natIP.flatten()
                if (natIps) it.vars['ansible_host'] = natIps[0]
            }
        }
    }
}

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

    case Operation.LIST_GCP:

        def fileName = arguments[Argument.FILE]
        def gcpProject = arguments[Argument.GCP_PROJECT]
        def gcpZone = arguments[Argument.GCP_ZONE]

        Inventory inventory = IniFactory.create(new File(fileName))

        processInventory(inventory, gcpProject, gcpZone)

        println inventory.render()

        break

    case Operation.HOST_GCP:

        def fileName = arguments[Argument.FILE]
        def host = arguments[Argument.HOST]
        def gcpProject = arguments[Argument.GCP_PROJECT]
        def gcpZone = arguments[Argument.GCP_ZONE]

        Inventory inventory = IniFactory.create(new File(fileName))

        processInventory(inventory, gcpProject, gcpZone)

        println inventory.renderHost(host)

        break

    default:
        throw new UnsupportedOperationException("$operation not supported yet")

}

