package ansible.model

class Group {
    String name
    Set<Group> children = []
    Set<Host> hosts = []
    Map<String,?> vars = [:]
}
