package ansible

import ansible.model.Group
import ansible.model.Host
import ansible.model.Inventory
import groovy.json.JsonSlurper

class JsonFactory {

    private Map<String, ?> inventory
    private Map<String,Host> hostRegistry = [:]
    private Map<String,Group> groupRegistry = [:]

    JsonFactory(Map<String, ?> inventory) {
        this.inventory = inventory
//       Map<String,?> grouped = inventory.groupBy { it.key == 'hosts' ? 'hosts' : 'groups' }
        this.inventory.keySet().each { getOrCreateGroup(it) }
    }

    static Inventory create(File file) {
        Map<String, ?> invData = new JsonSlurper().parse(file)
        def factory = new JsonFactory(invData)
        factory.createInventory()
    }

    Inventory createInventory() {
        new Inventory(hosts: hostRegistry.values(), groups: groupRegistry.values())
    }

    private Group getOrCreateGroup(String name) {
        if(groupRegistry.containsKey(name)) return groupRegistry[name]
        def group = createGroup(name, inventory[name])
        groupRegistry[name] = group
        group
    }

    private Group createGroup(String name, List<String> hosts) {
        createGroup(name, hosts, null, null)
    }

    private Group createGroup(String name, Map<String,?> data) {
        createGroup(name, data.hosts, data.children, data.vars)
    }

    private Group createGroup(String name, List<String> hosts, List<String> children, Map<String,?> vars ) {

        Group group = new Group(name: name)

        if (vars) group.vars.putAll(vars)

        hosts.each {
            def host = getOrCreateHost(it)
            host.groups << group
            group.hosts << host
        }

        children.each {
            def childGroup = getOrCreateGroup(it)
            childGroup.parents << group
            group.children << childGroup
        }

        group
    }

    private Host getOrCreateHost(String name) {

        if(hostRegistry.containsKey(name)) return hostRegistry[name]

        Host host = new Host(name: name)

        hostRegistry[name] = host

        host
    }
}
