import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.compute.Compute
import com.google.api.services.compute.ComputeScopes
import com.google.api.services.compute.model.InstanceList

HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport()
JsonFactory jsonFactory = JacksonFactory.getDefaultInstance()

// Authenticate using Google Application Default Credentials.
GoogleCredential credential = GoogleCredential.getApplicationDefault()

//don't want to investigate, taken as is
if (credential.createScopedRequired()) {
    List<String> scopes = [
            ComputeScopes.COMPUTE_READONLY, // Set Google Cloud Storage scope to Full Control
            ComputeScopes.COMPUTE // Set Google Compute Engine scope to Read-write.
    ]
    credential = credential.createScoped(scopes)
}

Compute compute =
        new Compute.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName('TEST LISTER')
                .build()

def projectId = 'mindful-atlas-188816'
def zoneName = 'europe-west1-d'

Compute.Instances.List instances = compute.instances().list(projectId, zoneName)
InstanceList list = instances.execute()

println list.toPrettyString()

println list.getItems()?.collectMany {
    println(it.name)
    [
            it.networkInterfaces.accessConfigs.natIP,
            it.networkInterfaces.networkIP
    ].flatten()
} ?: 'n/a'