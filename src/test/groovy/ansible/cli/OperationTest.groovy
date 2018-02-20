package ansible.cli

class OperationTest extends GroovyTestCase {

    void testList() {

        def args = ['--file', 'inventory.json', '--list']

        def argValues = Argument.collect(args)

        assert argValues[Argument.FILE] == 'inventory.json'
        assert argValues[Argument.LIST] == null

        assert Operation.of(argValues.keySet()) == Operation.LIST
    }

    void testHost() {

        def args = ['--file', 'inventory.json', '--host', 'ahost.com']

        def argValues = Argument.collect(args)

        assert argValues[Argument.FILE] == 'inventory.json'
        assert argValues[Argument.HOST] == 'ahost.com'

        assert Operation.of(argValues.keySet()) == Operation.HOST

    }

    void testNofile() {

        def args = ['--host', 'ahost.com']

        def argValues = Argument.collect(args)

        assert !argValues.containsKey(Argument.FILE)

        assert argValues[Argument.HOST] == 'ahost.com'

        shouldFail AssertionError.class, { Operation.of(argValues.keySet()) }
    }

    void testAllSet() {

        def args = ['--file', 'inventory.json', '--host', 'ahost.com', '--ololo', 'trololo', '--list']

        def argValues = Argument.collect(args)

        assert argValues[Argument.FILE] == 'inventory.json'
        assert argValues[Argument.HOST] == 'ahost.com'
        assert argValues[Argument.LIST] == null

        shouldFail AssertionError.class, { Operation.of(argValues.keySet()) }
    }
}
