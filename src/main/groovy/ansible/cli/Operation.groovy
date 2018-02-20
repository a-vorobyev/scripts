package ansible.cli

enum Operation {

    LIST(EnumSet.of(Argument.FILE, Argument.LIST)),
    HOST(EnumSet.of(Argument.FILE, Argument.HOST))

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