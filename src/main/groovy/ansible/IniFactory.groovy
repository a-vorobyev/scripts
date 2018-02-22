package ansible

import ansible.model.Group
import ansible.model.Host
import ansible.model.Inventory

import java.util.function.Predicate
import java.util.regex.Matcher

class IniFactory {

    private final String content
    private Map<String, Host> hostRegistry = [:]
    private Map<String, Group> groupRegistry = [:]


    IniFactory(String content) {
        this.content = content
    }

    static Inventory create(File file) {
        String text = file.getText('UTF-8')
        def factory = new IniFactory(text)
        factory.createInventory()
    }

    Inventory createInventory() {
        List<String> blocks = content.split(/\n+(?=\[)/)
        blocks.each { String block -> blockProcessors.any { it.test(block) } }
        new Inventory(hosts: hostRegistry.values(), groups: groupRegistry.values())
    }

    private Group getOrCreateGroup(String name) {

        if (groupRegistry.containsKey(name)) return groupRegistry[name]

        Group group = new Group(name: name)
        groupRegistry[name] = group
        group
    }

    private Host getOrCreateHost(String name) {

        if (hostRegistry.containsKey(name)) return hostRegistry[name]

        Host host = new Host(name: name)
        hostRegistry[name] = host
        host
    }

    private Host parseHost(String hostStr) {

        List<String> split1 = hostStr.trim().split(/\b\s+(?!=)|(?<!=)\s+\b/)
        String name = split1[0]
        Map<String, String> vars = split1.size() > 1 ?
                split1[1..-1].collectEntries {
                    List<String> split2 = it.split(/\=/)
                    [(split2[0].trim()): split2[1].trim()]
                }
                : [:]

        Host host = getOrCreateHost(name)
        host.vars = [*: host.vars, *: vars]
        host
    }

    private Predicate<String> hostBlockProcessor = { String block ->

        String _block = block.trim()

        if (_block =~ /^\[.*?]/) return false

        block.split(/\n/).each {
            parseHost it
        }
        true
    }


    private Predicate<String> groupBlockProcessor = { String block ->

        String _block = block.trim()

        Matcher m = _block =~ /(?s)^\[(\w+)]\s*\n(.*)$/

        if (!m) return false

        String name = m.group(1)
        String hostsStr = m.group(2)

        Group group = getOrCreateGroup name
        List<Host> hosts = hostsStr.split(/\n/).collect { parseHost it }
        group.hosts.addAll(hosts)

        true
    }

    private Predicate<String> groupVarsBlockProcessor = { String block ->

        String _block = block.trim()

        Matcher m = _block =~ /(?s)^\[(\w+):vars]\s*\n(.*)$/

        if (!m) return false

        String name = m.group(1)
        String varsStr = m.group(2)

        Map<String, String> vars = varsStr.split(/\n/).collectEntries {
            List<String> split = it.trim().split(/\=/, 2)
            split.size() > 1 ? [(split[0].trim()): split[1].trim()] : [:]
        }

        Group group = getOrCreateGroup name
        group.vars = [*: group.vars, *: vars]

        true
    }

    Predicate<String> groupChildrenBlockProcessor = { String block ->

        String _block = block.trim()

        Matcher m = _block =~ /(?s)^\[(\w+):children]\s*\n(.*)$/

        if (!m) return false

        String name = m.group(1)
        String childrenStr = m.group(2)

        List<Group> children = childrenStr.split(/\n/).collect {
            getOrCreateGroup it.trim()
        }

        Group group = getOrCreateGroup name
        group.children.addAll(children)

        true
    }

    private List<Predicate<String>> blockProcessors = [
            hostBlockProcessor,
            groupBlockProcessor,
            groupVarsBlockProcessor,
            groupChildrenBlockProcessor
    ]
}
