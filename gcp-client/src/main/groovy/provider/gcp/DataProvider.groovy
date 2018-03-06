package provider.gcp

import com.google.api.services.compute.Compute
import com.google.api.services.compute.model.Instance
import com.google.api.services.compute.model.InstanceList

class DataProvider {

    private final String appName
    private final ServiceFactory serviceFactory = new ServiceFactory()

    DataProvider(String appName) {
        this.appName = appName
    }

    List<Instance> getInstances(String projectId, String zoneName) {
        Compute compute = serviceFactory.createCompute(appName)
        Compute.Instances.List instances = compute.instances().list(projectId, zoneName)
        InstanceList list = instances.execute()
        list.getItems()
    }
}
