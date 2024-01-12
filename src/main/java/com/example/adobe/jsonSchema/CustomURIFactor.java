package com.example.adobe.jsonSchema;

import com.networknt.schema.uri.URIFactory;
import java.net.URI;

public class CustomURIFactor implements URIFactory {
  public static final String SCHEME = "custom";

  public CustomURIFactor() {
  }

  @Override public URI create(String uri) {
    try {
      return URI.create(uri);
    } catch (IllegalArgumentException var3) {
      throw new IllegalArgumentException("Unable to create URI.", var3);
    }
  }

  @Override public URI create(URI baseURI, String segment) {
    String urnPart = baseURI.getRawSchemeSpecificPart();
    int pos = urnPart.indexOf(58);
    String namespace = pos < 0 ? urnPart : urnPart.substring(0, pos);
    return URI.create("urn:" + namespace + ":" + segment);
  }
}
