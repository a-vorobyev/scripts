package ansible.model


class Inventory {

    Set<Group> groups
    Set<Host> hosts

    Map<String, ?> getGroupVars(Group group) {

        Map<String, ?> myVars = group.vars //collect from parent tree

        group.parents.inject([*: myVars]) { Map<String, ?> acc, Group parent ->

            getGroupVars(parent).each {
                if (!acc.containsKey(it.key)) acc[it.key] = it.value
            }
            acc
        }
    }

    Map<String, ?> getHostVars(Host host) {

        host.groups.inject([:]) { Map<String, ?> acc, Group group ->

            getGroupVars(group).each {
                if (!acc.containsKey(it.key)) acc[it.key] = it.value
            }
            acc
        }
    }

    Map<String, ?> getAllHostsVars() {

        hosts.collectEntries {
            [(it.name): getHostVars(it)]
        }
    }
}
