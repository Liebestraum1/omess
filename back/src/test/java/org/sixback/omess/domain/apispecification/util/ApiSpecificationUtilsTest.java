package org.sixback.omess.domain.apispecification.util;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.sixback.omess.domain.apispecification.util.ApiSpecificationUtils.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sixback.omess.domain.apispecification.exception.InvalidJsonSchemaException;

class ApiSpecificationUtilsTest {
	@Test
	@DisplayName("URI를 통한 path 생성 테스트")
	void generatePathTest(){
		//given
		String uri = "/api/v1/projects/1/api-specifications";

		//when
		String path = generatePath(uri, 1L);

		//then
		assertThat(path).isEqualTo("P1/A1");
	}

	@DisplayName("URI를 통한 상위 테이블의 추정 path 생성 테스트")
	@Test
	void generateEstimatedPatentPath() {
		//given
		String uri = "/api/v1/projects/1/api-specifications/2/domains";

		//when
		String estimatedParentPath = generateEstimatedParentPath(uri, 2L);

		//then
		assertThat(estimatedParentPath).isEqualTo("P1/A2");
	}

	@DisplayName("JSON SCHEMA 유효성 검증 테스트")
	@Test
	void checkIsValidJsonSchemaTest() {
		//given
		String validJsonSchema = "{\n"
			+ "  \"$schema\": \"http://json-schema.org/draft-07/schema#\",\n"
			+ "  \"title\": \"Product\",\n"
			+ "  \"description\": \"A product from Acme's catalog\",\n"
			+ "  \"type\": \"object\",\n"
			+ "  \"properties\": {\n"
			+ "    \"id\": {\n"
			+ "      \"description\": \"The unique identifier for a product\",\n"
			+ "      \"type\": \"integer\"\n"
			+ "    },\n"
			+ "    \"name\": {\n"
			+ "      \"description\": \"Name of the product\",\n"
			+ "      \"type\": \"string\"\n"
			+ "    },\n"
			+ "    \"price\": {\n"
			+ "      \"description\": \"The price of the product\",\n"
			+ "      \"type\": \"number\",\n"
			+ "      \"exclusiveMinimum\": 0\n"
			+ "    },\n"
			+ "    \"tags\": {\n"
			+ "      \"description\": \"Tags for the product\",\n"
			+ "      \"type\": \"array\",\n"
			+ "      \"items\": {\n"
			+ "        \"type\": \"string\"\n"
			+ "      },\n"
			+ "      \"minItems\": 1,\n"
			+ "      \"uniqueItems\": true\n"
			+ "    }\n"
			+ "  },\n"
			+ "  \"required\": [\"id\", \"name\"]\n"
			+ "}";

		String invalidJson = " "
			+ "    \"}$schema\": \"http://json-schema.org/draft-04/schema#\",\n"
			+ "            \"description\": \"The unique identifier for a product\",\n"
			+ "            \"type\": \"integer\"\n"
			+ "        },\n"
			+ "        \"name\": {\n"
			+ "            \"description\": \"Name of the product\",\n"
			+ "            \"type\": \"string\"\n"
			+ "        },\n"
			+ "        \"price\": {\n"
			+ "            \"type\": \"number\",\n"
			+ "            \"minimum\": 0,\n"
			+ "            \"exclusiveMinimum\": true\n"
			+ "        }}\n"
			+ "    },\n"
			+ "}";

		String invalidJsonSchema = "{\n"
			+ "  \"$schema\": \"http://json-schema.org/draft-07/schema#\",\n"
			+ "  \"title\": \"Person\",\n"
			+ "  \"type\": \"object\",\n"
			+ "  \"properties\": {\n"
			+ "    \"firstName\": {\n"
			+ "      \"type\": \"string\",\n"
			+ "      \"description\": \"The person's first name.\"\n"
			+ "    },\n"
			+ "    \"lastName\": {\n"
			+ "      \"type\": \"string\",\n"
			+ "      \"description\": \"The person's last name.\"\n"
			+ "    },\n"
			+ "    \"age\": {\n"
			+ "      \"description\": \"Age in years which must be equal to or greater than zero.\",\n"
			+ "      \"type\": \"integer\",\n"
			+ "      \"minimum\": 0\n"
			+ "    },\n"
			+ "    \"height\": {\n"
			+ "      \"type\": \"number\",\n"
			+ "      \"description\": \"The person's height in meters.\"\n"
			+ "    },\n"
			+ "    \"hobbies\": {\n"
			+ "      \"type\": \"array\",\n"
			+ "      \"items\": \"string\"\n"
			+ "    }\n"
			+ "  },\n"
			+ "  \"required\": [\"firstName\", \"lastName\", \"age\"]\n"
			+ "}";

		//when

		//then
		assertDoesNotThrow(() -> checkIsValidJsonSchema(validJsonSchema));
		assertThrowsExactly(InvalidJsonSchemaException.class,() -> checkIsValidJsonSchema(invalidJson));
	}
}