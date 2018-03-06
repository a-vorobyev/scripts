package provider

class ProviderFactory {

    HostDataProvider createHostDataProvider() {

        new HostDataProvider() {
            @Override
            Map<String, Object> getData() {
                return null
            }
        }
    }
}
