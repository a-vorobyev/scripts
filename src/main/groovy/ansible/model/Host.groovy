package ansible.model

class Host {

    String name
    List<Group> groups = []
    Map<String,?> vars = [:]
}
