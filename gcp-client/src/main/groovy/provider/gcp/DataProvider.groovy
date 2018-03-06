package provider.gcp

import com.google.api.services.compute.Compute
import com.google.api.services.compute.model.Instance
import com.google.api.services.compute.model.InstanceList

class DataProvider {

    private static final String APP_NAME = 'APIDR04ER'
    private final ServiceFactory serviceFactory = new ServiceFactory()

    List<Instance> getInstances(String projectId, String zoneName) {
        Compute compute = serviceFactory.createCompute(APP_NAME)
        Compute.Instances.List instances = compute.instances().list(projectId, zoneName)
        InstanceList list = instances.execute()
        list.getItems()
    }
}
