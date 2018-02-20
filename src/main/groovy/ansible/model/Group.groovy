package ansible.model

class Group {
    String name
    List<Group> parents = []
    List<Group> children = []
    List<Host> hosts = []
    Map<String,?> vars = [:]
}
