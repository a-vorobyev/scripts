import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.compute.Compute
import com.google.api.services.compute.ComputeScopes
import com.google.api.services.compute.model.Instance
import com.google.api.services.compute.model.InstanceList

HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport()
JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance()

// Authenticate using Google Application Default Credentials.
GoogleCredential credential = GoogleCredential.getApplicationDefault()

if (credential.createScopedRequired()) {
    List<String> scopes = new ArrayList<>();
    // Set Google Cloud Storage scope to Full Control.
    scopes.add(ComputeScopes.DEVSTORAGE_FULL_CONTROL)
    // Set Google Compute Engine scope to Read-write.
    scopes.add(ComputeScopes.COMPUTE)
    credential = credential.createScoped(scopes);
}

Compute compute =
        new Compute.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName('TEST LISTER')
                .build()
/*
urrent Properties:
  [core]
    project: [mindful-atlas-188816]
    account: [avorobyev.gcp@gmail.com]
    disable_usage_reporting: [True]
  [compute]
    region: [europe-west1]
    zone: [europe-west1-d]
 */

def PROJECT_ID = 'mindful-atlas-188816'
def ZONE_NAME = 'europe-west1-d'

//public static void printInstances(Compute compute) throws IOException {
System.out.println("================== Listing Compute Engine Instances ==================");
Compute.Instances.List instances = compute.instances().list(PROJECT_ID, ZONE_NAME);
InstanceList list = instances.execute();

if (list.getItems() == null) {
    System.out.println("No instances found. Sign in to the Google Developers Console and create "
            + "an instance at: https://console.developers.google.com/");
} else {
    for (Instance instance : list.getItems()) {
        System.out.println(instance.toPrettyString());

    }
}
//   return found;
//}