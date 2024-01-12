package com.example.adobe.jsonSchema;

import com.networknt.schema.uri.URIFetcher;
import com.networknt.schema.uri.URLFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CustomFileFetcher implements URIFetcher {
  public static final Set<String> SUPPORTED_SCHEMES;
  private Map<String, InputStream> map;

  static {
    SUPPORTED_SCHEMES = Collections.unmodifiableSet(new HashSet(
        Arrays.asList("custom")));
  }

  public CustomFileFetcher() {
    map = new HashMap<>();
    loadData();
  }

  @Override public InputStream fetch(URI uri) throws IOException {
    URLConnection conn = uri.toURL().openConnection();
    return this.getData(uri.toString());
  }

  private InputStream getData(String key) {
    if (map.containsKey(key))
      return map.getOrDefault(key, null);
    return null;
  }

  private void loadData() {
    InputStream is = CustomFileFetcher.class.getResourceAsStream("/core_annotation.json");
    map.put("core_annotation.json", is);
    System.out.println("schema loaded success...");
  }

}