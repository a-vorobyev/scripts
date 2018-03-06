package ansible.cli

enum Argument {

    FILE('--file'), HOST('--host'), LIST('--list'), GCP_PROJECT('--gcp-project'), GCP_ZONE('--gcp-zone')

    Argument(String key) {
        this.key = key
    }

    private final String key

    static Map<Argument, String> collect(List<String> args) {

        def result = values().findAll {
            args.contains(it.key)
        }.collectEntries {
            int idx = args.indexOf(it.key)
            assert idx != -1 //impossible
            [(it): idx < args.size() - 1 ? args[idx + 1] : null]
        }

        assert result, "bad args: $args"

        result
    }
}