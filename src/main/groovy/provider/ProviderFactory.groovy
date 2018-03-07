package provider

import provider.gcp.DataProvider

class ProviderFactory {

    static HostDataProvider createGcpHostDataProvider(String appName, String projectId, String zoneName) {

        DataProvider gcpDataProvider = new DataProvider(appName)

        new HostDataProvider() {
            @Override
            Map<String, Object> getData() {
                gcpDataProvider.getInstances(projectId, zoneName)?.collectEntries { [(it.name): it] } ?: [:]
            }
        }
    }
}
