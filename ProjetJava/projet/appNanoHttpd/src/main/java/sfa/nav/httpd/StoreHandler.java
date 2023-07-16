package sfa.nav.httpd;

public static class StoreHandler extends GeneralHandler {
    @Override
    public Response get(
        // curl 'http://localhost:8080/stores/123' 
      UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
        return newFixedLengthResponse("Retrieving store for id = "
          + urlParams.get("storeId"));
    }
}