package ansible

import groovy.json.JsonOutput

println new File('.').absolutePath

def inventory = Inventory.parse('src/main/resources/inventory.json')

println inventory.groups

println inventory.allHostsVars



