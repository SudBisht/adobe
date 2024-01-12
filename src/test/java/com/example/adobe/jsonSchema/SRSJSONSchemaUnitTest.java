package com.example.adobe.jsonSchema;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import org.junit.Test;

public class SRSJSONSchemaUnitTest {
  private ObjectMapper mapper = new ObjectMapper();

  @Test
  public void validInput_whenValidating_thenValidSRSCore1() throws IOException {

    JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V6);
    JsonSchema jsonSchema =
        factory.getSchema(SRSJSONSchemaUnitTest.class.getResourceAsStream("/core_annotation.json"));

    JsonNode jsonNode =
        mapper.readTree(
            SRSJSONSchemaUnitTest.class.getResourceAsStream("/core_api_valid_input.json"));
    Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
    assertThat(errors).isEmpty();
  }

  @Test
  public void givenInvalidInput_whenValidating_thenInvalidCore1() throws IOException {

    JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V6);
    JsonSchema jsonSchema =
        factory.getSchema(SRSJSONSchemaUnitTest.class.getResourceAsStream("/core_annotation.json"));

    JsonNode jsonNode =
        mapper.readTree(
            SRSJSONSchemaUnitTest.class.getResourceAsStream("/core_api_invalid_input.json"));
    Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
    assertThat(errors).isNotEmpty()
        .asString()
        .contains(
            "[$.repo:etag: is not defined in the schema and the schema does not allow additional "
                + "properties]");
  }

  @Test
  public void validInput_whenValidating_thenValidSRSDelete2() throws IOException {

    JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V6);
    JsonSchema jsonSchema =
        factory.getSchema(SRSJSONSchemaUnitTest.class.getResourceAsStream("/delete_schema.json"));

    JsonNode jsonNode =
        mapper.readTree(SRSJSONSchemaUnitTest.class.getResourceAsStream("/delete_valid.json"));
    Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
    assertThat(errors).isEmpty();
  }

  @Test
  public void inValidInput_whenValidating_thenValidSRSDelete2() throws IOException {

    JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V6);
    JsonSchema jsonSchema =
        factory.getSchema(SRSJSONSchemaUnitTest.class.getResourceAsStream("/delete_schema.json"));

    JsonNode jsonNode =
        mapper.readTree(SRSJSONSchemaUnitTest.class.getResourceAsStream("/delete_invalid.json"));
    Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
    assertThat(errors).isNotEmpty()
        .asString()
        .contains("");
  }

  private static Set<String> supportedFieldAttrNames = Sets.newHashSet("id",
      "xdm:flightStatusSearch", "description:");

  //@Ignore
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

  //@Ignore
  @Test
  public void inValidInput_whenValidating_thenValidSRSAnnotation3() throws IOException {

    JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V6);
    JsonSchema jsonSchema =
        factory.getSchema(SRSJSONSchemaUnitTest.class.getResourceAsStream("/annotation.json"));

    JsonNode jsonNode =
        mapper.readTree(
            SRSJSONSchemaUnitTest.class.getResourceAsStream("/annotation_invalid.json"));
    Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
    assertThat(errors).isNotEmpty()
        .asString()
        .contains("");
  }

  @Test
  public void validInput_whenValidating_thenValidSRSPUPAMeta4() throws IOException {

    JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V6);
    JsonSchema jsonSchema =
        factory.getSchema(SRSJSONSchemaUnitTest.class.getResourceAsStream("/pupa_meta.json"));

    JsonNode jsonNode =
        mapper.readTree(SRSJSONSchemaUnitTest.class.getResourceAsStream("/pupa_meta_valid.json"));
    Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
    assertThat(errors).isEmpty();
  }

  @Test
  public void inValidInput_whenValidating_thenValidSRSPUPAMeta4() throws IOException {

    JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V6);
    JsonSchema jsonSchema =
        factory.getSchema(SRSJSONSchemaUnitTest.class.getResourceAsStream("/pupa_meta.json"));

    JsonNode jsonNode =
        mapper.readTree(SRSJSONSchemaUnitTest.class.getResourceAsStream("/pupa_meta_invalid.json"));
    Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
    assertThat(errors).isNotEmpty()
        .asString()
        .contains("$.collab:annotations: array found, object expected");
  }

  @Test
  public void validInput_whenValidating_thenValidSRSPUPAAnnotation5() throws IOException {

    JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V6);
    JsonSchema jsonSchema =
        factory.getSchema(SRSJSONSchemaUnitTest.class.getResourceAsStream("/pupa_anno.json"));

    JsonNode jsonNode =
        mapper.readTree(SRSJSONSchemaUnitTest.class.getResourceAsStream("/pupa_anno_valid.json"));
    Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
    assertThat(errors).isEmpty();
  }

  @Test
  public void inValidInput_whenValidating_thenValidSRSPUPAAnnotation5() throws IOException {

    JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V6);
    JsonSchema jsonSchema =
        factory.getSchema(SRSJSONSchemaUnitTest.class.getResourceAsStream("/pupa_anno.json"));

    JsonNode jsonNode =
        mapper.readTree(SRSJSONSchemaUnitTest.class.getResourceAsStream("/pupa_anno_invalid.json"));
    Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
    assertThat(errors).isNotEmpty()
        .asString()
        .contains("$.collab:readStatus: string found, boolean expected");
  }

  @Test
  public void validInput_whenValidating_thenValidSRSCollection6() throws IOException {

    JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V6);
    JsonSchema jsonSchema =
        factory.getSchema(SRSJSONSchemaUnitTest.class.getResourceAsStream("/collection_meta.json"));

    JsonNode jsonNode =
        mapper.readTree(SRSJSONSchemaUnitTest.class.getResourceAsStream("/collection_valid.json"));
    Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
    assertThat(errors).isEmpty();
  }

  @Test
  public void inValidInput_whenValidating_thenValidSRSCollection6() throws IOException {

    JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V6);
    JsonSchema jsonSchema =
        factory.getSchema(SRSJSONSchemaUnitTest.class.getResourceAsStream("/collection_meta.json"));

    JsonNode jsonNode =
        mapper.readTree(
            SRSJSONSchemaUnitTest.class.getResourceAsStream("/collection_invalid.json"));
    Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
    assertThat(errors).isNotEmpty().asString()
        .contains("$.collab:count: string found, integer expected");
  }
}
