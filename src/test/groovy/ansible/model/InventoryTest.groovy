package ansible.model

import ansible.JsonFactory

/*
{
  "databases": {
    "hosts": ["host1.example.com", "host2.example.com", "host7.example.com"],
    "vars": {
      "a": true
    }
  },
  "webservers": ["host2.example.com", "host3.example.com"],
  "atlanta": {
    "hosts": ["host1.example.com", "host4.example.com", "host5.example.com"],
    "vars": {
      "b": false
    },
    "children": ["marietta", "5points"]
  },
  "marietta": ["host6.example.com"],
  "5points": { "hosts": ["host7.example.com"], "vars": { "b": "awesome" }, "children": ["marietta"]}
}
 */

class InventoryTest extends GroovyTestCase {

    URL inventoryUrl = getClass().classLoader.getResource('inventory.json')

    Inventory inventory = JsonFactory.create(new File(inventoryUrl.toURI()))

    void testConsitency() {

        assert inventory.groups.size() == 5

        assert inventory.hosts.size() == 7

        def host2 = inventory.hosts.find { it.name == 'host2.example.com' }

        assert host2.groups.size() == 2

        assert host2.groups*.name == ['databases', 'webservers']

        def host7 = inventory.hosts.find { it.name == 'host7.example.com' }

        assert host7.groups.size() == 2

        assert host7.groups*.name == ['databases', '5points']
    }


    void testGetAllHostsVars() {

        def vars = inventory.allHostsVars

        assert vars['host1.example.com'] == [a: true, b: false]
        assert vars['host2.example.com'] == [a: true]
        assert vars['host3.example.com'] == [:]
        assert vars['host4.example.com'] == [b: false]
        assert vars['host5.example.com'] == [b: false]
        assert vars['host6.example.com'] == [b: false]
        assert vars['host7.example.com'] == [a: true, b: 'awesome']

    }
}
