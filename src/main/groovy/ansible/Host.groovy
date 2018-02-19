package ansible

class Host {

    String name
    List<Group> groups = []
    Map<String,?> vars = [:]
}
