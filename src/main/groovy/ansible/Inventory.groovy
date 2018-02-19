package ansible

import groovy.json.JsonSlurper

class Inventory {

    private Map<String,?> inventory

    private Map<String,Host> hostRegistry = [:]
    private Map<String,Group> groupRegistry = [:]

    Inventory(Map<String, ?> inventory) {
        this.inventory = inventory
        inventory.keySet().each { createGroup(it) }
    }

    private Group createGroup(String name) {
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
            def host = createHost(it)
            host.groups << group
            group.hosts << host
        }

        children.each {
            def childGroup = createGroup it
            childGroup.parents << group
            group.children << childGroup
        }

        group
    }

    private Host createHost(String name) {

        if(hostRegistry.containsKey(name)) return hostRegistry[name]

        Host host = new Host(name: name)

        hostRegistry[name] = host

        host
    }

    static Inventory parse(String path) {

        Map<String,?> invData = new JsonSlurper().parse( new File(path) )

//        Map<String,?> grouped = invData.groupBy { it.key == 'hosts' ? 'hosts' : 'groups' }

        new Inventory(invData)
    }

    List<Group> getGroups() {
        new ArrayList<>(groupRegistry.values())
    }

    List<Host> getHosts() {
        new ArrayList<>(hostRegistry.values())
    }

    Group getGroup(String name) {
        groupRegistry[name]
    }

    Host getHost(String name) {
        hostRegistry[name]
    }

    Map<String,?> getGroupVars(Group group) {

        Map<String,?> myVars = group.vars //collect from parent tree

        group.parents.inject( [ *:myVars ] ) { Map<String,?> acc, Group parent ->

            getGroupVars(parent).each {
                if (!acc.containsKey(it.key)) acc[it.key] = it.value
            }
            acc
        }
    }

    Map<String,?> getHostVars(Host host) {

        host.groups.inject([:]) { Map<String,?> acc, Group group ->

            getGroupVars(group).each {
                if (!acc.containsKey(it.key)) acc[it.key] = it.value
            }
            acc
        }
    }

    Map<String,?> getAllHostsVars() {

        hostRegistry.collectEntries { k,v ->

            [ (k):getHostVars(v) ]
        }
    }
}
