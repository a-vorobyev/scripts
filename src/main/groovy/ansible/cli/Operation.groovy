package ansible.cli

enum Operation {

    LIST(EnumSet.of(Argument.FILE, Argument.LIST)),
    HOST(EnumSet.of(Argument.FILE, Argument.HOST)),
    LIST_GCP(EnumSet.of(Argument.FILE, Argument.LIST, Argument.GCP_PROJECT, Argument.GCP_ZONE)),
    HOST_GCP(EnumSet.of(Argument.FILE, Argument.HOST, Argument.GCP_PROJECT, Argument.GCP_ZONE))

    Operation(Set<Argument> args) {
        this.args = args
    }

    private final Set<Argument> args

    static Operation of(Set<Argument> args) {
        def _es = EnumSet.copyOf(args)
        Operation operation = values().find {
            it.args == _es
        }

        assert operation, "not sensible args combination: $args"

        operation
    }
}