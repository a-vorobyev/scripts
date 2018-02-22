package ansible.model

import ansible.IniFactory
import org.junit.Ignore

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

    @Ignore
    void testConsitency() {

        assert inventory.groups.size() == 5

        assert inventory.hosts.size() == 7

//        def host2 = inventory.hosts.find { it.name == 'host2.example.com' }

//        assert host2.groups.size() == 2
//
//        assert host2.groups*.name == ['databases', 'webservers']
//
//        def host7 = inventory.hosts.find { it.name == 'host7.example.com' }
//
//        assert host7.groups.size() == 2
//
//        assert host7.groups*.name == ['databases', '5points']
    }

    @Ignore
    void testGetAllHostsVars() {

//        def vars = inventory.allHostsVars
//
//        assert vars['host1.example.com'] == [a: true, b: false]
//        assert vars['host2.example.com'] == [a: true]
//        assert vars['host3.example.com'] == [:]
//        assert vars['host4.example.com'] == [b: false]
//        assert vars['host5.example.com'] == [b: false]
//        assert vars['host6.example.com'] == [b: false]
//        assert vars['host7.example.com'] == [a: true, b: 'awesome']

    }
}
