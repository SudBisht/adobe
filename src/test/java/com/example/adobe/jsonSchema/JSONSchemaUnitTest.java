package com.example.adobe.jsonSchema;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.google.common.collect.Sets;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class JSONSchemaUnitTest {

  public static final String DOLLAR_REF = "$ref";
  public static final String PROPERTIES = "properties";
  public static final String ALL_OF = "allOf";
  public static final String NS_ADOBE_COM = "https://ns.adobe.com/";
  public static final String DOT = ".";
  public static final String HASH = "#";
  public static final String SLASH = "/";
  public static final String HTTP = "http";

  //Accept Media
  private static final Set<String> keywords =
      Sets.newHashSet(
          "additionalItems", "items", "contains", "additionalProperties:", "propertyNames", "not");

  private static final Set<String> arrayKeywords =
      Sets.newHashSet("items", "allOf", "anyOf", "oneOf");

  private static final Set<String> propsKeywords =
      Sets.newHashSet("definitions", "properties", "patternProperties", "dependencies");

  private static final Set<String> skipKeywords =
      Sets.newHashSet(
          "default",
          "enum",
          "const",
          "required",
          "maximum",
          "minimum",
          "exclusiveMaximum",
          "exclusiveMinimum",
          "multipleOf",
          "maxLength",
          "minLength",
          "pattern",
          "format",
          "maxItems",
          "minItems",
          "uniqueItems",
          "maxProperties",
          "minProperties");

  private ObjectMapper mapper = new ObjectMapper();

  private static Set<String> supportedFieldAttrNames = Sets.newHashSet("id",
      "xdm:flightStatusSearch", "description:");

  @Test
  public void validInput_whenValidating_thenValidSRSAnnotation3()
      throws IOException {

    JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V6);

    JsonSchema jsonSchema =
        factory.getSchema(SRSJSONSchemaUnitTest.class.getResourceAsStream("/annotation.json"));

    Iterator<String> fieldNames = jsonSchema.getSchemaNode().get("properties").fieldNames();

    fieldNames.forEachRemaining(fieldName -> {
      supportedFieldAttrNames.add(fieldName);
    });

    System.out.println("done...");
    JsonNode jsonNode =
        mapper.readTree(SRSJSONSchemaUnitTest.class.getResourceAsStream("/annotation_valid.json"));
    Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
    assertThat(errors).isEmpty();
  }

  public static Set<TextNode> getReferences(
      JsonNode obj, boolean onlyAllOf, boolean excludeHashReference) {
    Set<TextNode> refs = new HashSet();
    JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
    String jsonPtr = StringUtils.EMPTY;
    final String SLASH_ALLOF = SLASH + ALL_OF;
    final String SLASH_PROP_SLASH = SLASH + PROPERTIES + SLASH;
    traverse(
        obj,
        jsonPtr,
        obj,
        jsonPtr,
        jsonPtr,
        null,
        null,
        true,
        new TraverseJsonInterceptor() {
          @Override
          public void onJsonNode(
              JsonNode schema,
              String jsonPtr,
              JsonNode rootSchema,
              String parentJsonPtr,
              String parentKeyword,
              JsonNode parentSchema,
              Object keyIndex,
              boolean isAllKey) {
            if (!schema.isObject()) {
              return;
            }
            if (onlyAllOf) {
              if (jsonPtr.startsWith(SLASH_ALLOF) && jsonPtr.indexOf(SLASH_PROP_SLASH) == -1) {
                if (schema.has(DOLLAR_REF)) {
                  String ref = getURIFromAltId(schema.get(DOLLAR_REF).textValue());
                  ObjectNode node = (ObjectNode) schema;
                  node.put(DOLLAR_REF, ref);
                  String[] parent = ref.split(HASH);
                  if (excludeHashReference && ArrayUtils.getLength(parent) > 1) {
                    return;
                  }
                  if (parent[0].length() > 0) {
                    refs.add(nodeFactory.textNode(parent[0]));
                  }
                }
              }
            } else if (schema.has(DOLLAR_REF)) {
              String ref = getURIFromAltId(schema.get(DOLLAR_REF).textValue());
              ObjectNode node = (ObjectNode) schema;
              node.put(DOLLAR_REF, ref);
              String[] parent = ref.split(HASH);
              if (parent[0].length() > 0) {
                refs.add(nodeFactory.textNode(parent[0]));
              }
            }
          }
        });
    return refs;
  }

  public static String getURIFromAltId(String id) {
    if (isUriFormat(id) || StringUtils.startsWith(id, "#")) {
      return id;
    } else if (id.startsWith("_schema.org")) {
      String temp = id.substring(11);
      return "http://schema.org/" + StringUtils.join(StringUtils.split(temp, DOT), SLASH);
    } else if (id.matches("_ns\\..+\\.com.+")) {
      int temp = StringUtils.indexOf(id, ".com") + 4;
      String path = StringUtils.substring(id, temp);
      return "https://"
          + StringUtils.substring(id, 1, temp)
          + SLASH
          + StringUtils.join(StringUtils.split(path, DOT), SLASH);
    } else if (id.endsWith(DOT)) {
      // Handles cases where the original $id ended with a slash
      return NS_ADOBE_COM
          + StringUtils.substring(StringUtils.join(StringUtils.split(id, DOT), SLASH), 1)
          + SLASH;
    }
    return NS_ADOBE_COM
        + StringUtils.substring(StringUtils.join(StringUtils.split(id, DOT), SLASH), 1);
  }

  public static void traverse(
      JsonNode schema,
      String jsonPtr,
      JsonNode rootSchema,
      String parentJsonPtr,
      String parentKeyword,
      JsonNode parentSchema,
      Object keyIndex,
      boolean isAllKey,
      TraverseJsonInterceptor interceptor) {

    if (schema.isObject()) {
      interceptor.onJsonNode(
          schema,
          jsonPtr,
          rootSchema,
          parentJsonPtr,
          parentKeyword,
          parentSchema,
          keyIndex,
          isAllKey);

      Map<String, JsonNode> fields = new ConcurrentSkipListMap();
      schema
          .fields()
          .forEachRemaining(
              entry -> {
                fields.put(entry.getKey(), entry.getValue());
              });

      for (Map.Entry<String, JsonNode> entry : fields.entrySet()) {

        String key = entry.getKey();
        JsonNode value = entry.getValue();

        if (value.isArray()) {
          if (arrayKeywords.contains(key)) {

            ArrayNode arrayNode = (ArrayNode) value;

            for (int i = 0; i < arrayNode.size(); i++) {
              traverse(
                  arrayNode.get(i),
                  jsonPtr + SLASH + key + SLASH + i,
                  rootSchema,
                  jsonPtr,
                  key,
                  schema,
                  i,
                  isAllKey,
                  interceptor);
            }
          }
        } else if (propsKeywords.contains(key)) {
          if (value.isObject()) {
            Map<String, JsonNode> fields1 = new ConcurrentSkipListMap();
            value
                .fields()
                .forEachRemaining(
                    v -> {
                      fields1.put(v.getKey(), v.getValue());
                    });

            for (Map.Entry<String, JsonNode> prop : fields1.entrySet()) {
              final String propKey = prop.getKey();
              traverse(
                  prop.getValue(),
                  jsonPtr + SLASH + key + SLASH + escapeJsonPtr(propKey),
                  rootSchema,
                  jsonPtr,
                  key,
                  schema,
                  propKey,
                  isAllKey,
                  interceptor);
            }
          }
        } else if (keywords.contains(key) || (isAllKey && !(skipKeywords.contains(key)))) {
          traverse(
              value,
              jsonPtr + SLASH + key,
              rootSchema,
              jsonPtr,
              key,
              schema,
              null,
              isAllKey,
              interceptor);
        }
      }
    }
  }

  public static boolean isUriFormat(String origString) {
    return StringUtils.startsWith(origString, HTTP);
  }

  public static String escapeJsonPtr(String str) {
    return str.replace("~", "~0").replace(SLASH, "~1");
  }

  public static String unescapeJsonPtr(String str) {
    return str.replace("~1", "/").replace("~", "~0");
  }

  public interface TraverseJsonInterceptor {
    /**
     * This function is called on each of the JSON objects found within the schema.
     * The caller can implement what logic they want to execute on each object
     * @param schema
     * @param jsonPtr
     * @param rootSchema
     * @param parentJsonPtr
     * @param parentKeyword
     * @param parentSchema
     * @param keyIndex
     * @param isAllKey
     */
    void onJsonNode(JsonNode schema, String jsonPtr,
        JsonNode rootSchema, String parentJsonPtr, String parentKeyword, JsonNode parentSchema,
        Object keyIndex, boolean isAllKey);

  }
}
