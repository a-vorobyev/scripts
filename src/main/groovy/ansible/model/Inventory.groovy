package ansible.model

import groovy.json.JsonOutput

class Inventory {

    Set<Group> groups
    Set<Host> hosts

    String render() {

        def invGroups = groups.findAll { it.hosts || it.children || it.vars }.collectEntries {

            def data = null

            if (it.hosts && !(it.vars || it.children)) {
                data = it.hosts.name
            } else if (it.vars || it.children) {
                data = [:]
                if (it.vars) data.vars = it.vars
                if (it.children) data.children = it.children.name
                if (it.hosts) data.hosts = it.hosts.name
            }

            [(it.name): data]
        }

        def invHosts = hosts.collectEntries {
            [(it.name): it.vars]
        }

        def inventory = [
                _meta: [
                        hostvars: invHosts
                ],
                *    : invGroups
        ]

        JsonOutput.prettyPrint(JsonOutput.toJson(inventory))
    }

    String renderHost(String name) {

        Host host = hosts.find { it.name == name }
        assert host, "there is no host $name in inventory"

        JsonOutput.prettyPrint(JsonOutput.toJson(host.vars))
    }
}
