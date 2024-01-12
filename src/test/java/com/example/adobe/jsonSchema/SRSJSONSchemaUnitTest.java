package com.example.adobe.jsonSchema;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;

public class SRSJSONSchemaUnitTest {
  private ObjectMapper mapper = new ObjectMapper();

  @Test
  public void validInput_Core1() throws IOException {

    JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V6);
    JsonSchema jsonSchema = factory.getSchema(readFile("schema/core-annotation.schema.json"));
    JsonNode jsonNode = mapper.readTree(readFile("schema/core-annotation.example.1.json"));

    Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
    assertThat(errors).isEmpty();
  }

  @Test
  public void givenInvalidInput_Core1() throws IOException {

    JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V6);
    JsonSchema jsonSchema = factory.getSchema(readFile("schema/core-annotation.schema.json"));
    JsonNode jsonNode = mapper.readTree(readFile("schema/core-annotation.invalid.1.json"));

    Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
    assertThat(errors).isNotEmpty()
        .asString()
        .contains(
            "[$.repo:etag: is not defined in the schema and the schema does not allow additional "
                + "properties]");
  }

  @Test
  public void validInput_Delete2() throws IOException {

    JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V6);
    JsonSchema jsonSchema = factory.getSchema(readFile("schema/deleted-annotation.schema.json"));
    JsonNode jsonNode = mapper.readTree(readFile("schema/deleted-annotation.example.1.json"));

    Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
    assertThat(errors).isEmpty();
  }

  @Test
  public void inValidInput_Delete2() throws IOException {

    JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V6);
    JsonSchema jsonSchema = factory.getSchema(readFile("schema/deleted-annotation.schema.json"));
    JsonNode jsonNode = mapper.readTree(readFile("schema/deleted-annotation.invalid.1.json"));

    Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
    assertThat(errors).isNotEmpty()
        .asString()
        .contains("");
  }

  @Test
  public void validInput_Annotation3() throws IOException {

    JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V6);
    JsonSchema jsonSchema = factory.getSchema(readFile("schema/annotation.schema.json"));
    JsonNode jsonNode = mapper.readTree(readFile("schema/annotation.example.1.json"));

    Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
    assertThat(errors).isNotEmpty()
        .asString()
        .contains(
            "$.collab:coreAnnotation.repo:etag: is not defined in the schema and the schema does "
                + "not allow additional properties");
  }

  @Test
  public void inValidInput_Annotation3() throws IOException {

    JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V6);
    JsonSchema jsonSchema = factory.getSchema(readFile("schema/annotation.schema.json"));
    JsonNode jsonNode = mapper.readTree(readFile("schema/annotation.invalid.1.json"));

    Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
    assertThat(errors).isNotEmpty()
        .asString()
        .contains("");
  }

  @Test
  public void validInput_PUPAMeta4() throws IOException {

    JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V6);
    JsonSchema jsonSchema = factory.getSchema(readFile("schema/per-user-metadata.schema.json"));
    JsonNode jsonNode = mapper.readTree(readFile("schema/per-user-metadata.example.1.json"));

    Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
    assertThat(errors).isEmpty();
  }

  @Test
  public void inValidInput_PUPAMeta4() throws IOException {

    JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V6);
    JsonSchema jsonSchema = factory.getSchema(readFile("schema/per-user-metadata.schema.json"));
    JsonNode jsonNode = mapper.readTree(readFile("schema/per-user-metadata.invalid.1.json"));

    Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
    assertThat(errors).isNotEmpty()
        .asString()
        .contains("$.collab:annotations: array found, object expected");
  }

  @Test
  public void validInput_PUPAAnnotation5() throws IOException {

    JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V6);
    JsonSchema jsonSchema =
        factory.getSchema(readFile("schema/per-user-per-annotation.schema.json"));
    JsonNode jsonNode = mapper.readTree(readFile("schema/per-user-per-annotation.example.1.json"));

    Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
    assertThat(errors).isEmpty();
  }

  @Test
  public void inValidInput_PUPAAnnotation5() throws IOException {

    JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V6);
    JsonSchema jsonSchema =
        factory.getSchema(readFile("schema/per-user-per-annotation.schema.json"));
    JsonNode jsonNode = mapper.readTree(readFile("schema/per-user-per-annotation.invalid.1.json"));

    Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
    assertThat(errors).isNotEmpty()
        .asString()
        .contains("$.collab:readStatus: string found, boolean expected");
  }

  @Test
  public void validInput_Collection6() throws IOException {

    JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V6);
    JsonSchema jsonSchema = factory.getSchema(readFile("schema/collection-metadata.schema.json"));
    JsonNode jsonNode = mapper.readTree(readFile("schema/collection-metadata.example.1.json"));

    Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
    assertThat(errors).isEmpty();
  }

  @Test
  public void inValidInput_Collection6() throws IOException {

    JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V6);
    JsonSchema jsonSchema = factory.getSchema(readFile("schema/collection-metadata.schema.json"));
    JsonNode jsonNode = mapper.readTree(readFile("schema/collection-metadata.invalid.1.json"));

    Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
    assertThat(errors).isNotEmpty().asString()
        .contains("$.collab:count: string found, integer expected");
  }

  @Ignore
  @Test
  public void validInput_rhmergerinfo7() throws IOException {

    JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V6);
    JsonSchema jsonSchema = factory.getSchema(readFile("schema/rhmergeinfo.schema.json"));
    JsonNode jsonNode = mapper.readTree(readFile("schema/rhmergeinfo.example.1.json"));

    Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
    assertThat(errors).isEmpty();
  }

  @Ignore
  @Test
  public void inValidInput_rhmergerinfo7() throws IOException {

    JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V6);
    JsonSchema jsonSchema = factory.getSchema(readFile("schema/rhmergeinfo.schema.json"));
    JsonNode jsonNode = mapper.readTree(readFile("schema/rhmergeinfo.invalid.1.json"));

    Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
    assertThat(errors).isEmpty();
  }

  protected String readFile(String filename) throws IOException {
    ClassLoader classLoader = this.getClass().getClassLoader();

    return IOUtils.toString(
        Objects.requireNonNull(classLoader.getResourceAsStream(filename)),
        StandardCharsets.UTF_8.name());
  }
}