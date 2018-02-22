package ansible.model

import ansible.IniFactory

/*
ololo a=2
trololo a=33

[atlanta]
host1
host2

[raleigh]
host2
host3

[southeast:children]
atlanta
raleigh

[southeast:vars]
some_server=foo.southeast.example.com
halon_system_timeout=30
self_destruct_countdown=60
escape_pods=2

[usa:children]
southeast
northeast
southwest
northwest
 */

class InventoryTest extends GroovyTestCase {

    URL inventoryUrl = getClass().classLoader.getResource('inventory.ini')

    Inventory inventory = IniFactory.create(new File(inventoryUrl.toURI()))

    void testConsitency() {

        assert inventory.groups.size() == 7

        assert inventory.hosts.size() == 5

        def southeast = inventory.groups.find { it.name == 'southeast' }

        assert southeast.children.size() == 2
//
        assert southeast.children.name == ['atlanta', 'raleigh']
    }

    void testGetAllHostsVars() {

        inventory.hosts.find { it.name == 'ololo' }.vars == [a: 2]

    }
}
