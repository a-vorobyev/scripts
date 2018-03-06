package provider.gcp;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.compute.Compute
import com.google.api.services.compute.ComputeScopes


class ServiceFactory {

    private HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport()
    private JsonFactory jsonFactory = JacksonFactory.getDefaultInstance()
    private GoogleCredential credential = GoogleCredential.getApplicationDefault()

    Compute createCompute(String appName) {

        if (credential.createScopedRequired()) {
            def scopes = [
                    ComputeScopes.COMPUTE_READONLY,
                    ComputeScopes.COMPUTE
            ]
            credential = credential.createScoped(scopes)
        }

        new Compute.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName(appName)
                .build()
    }
}
