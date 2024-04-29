package org.sixback.omess.domain.apispecification.util;

import static org.sixback.omess.domain.apispecification.exception.ApiSpecificationErrorMessage.*;

import org.sixback.omess.domain.apispecification.exception.InvalidApiInputException;
import org.springframework.boot.json.GsonJsonParser;

public class ApiSpecificationUtils {
    private static final GsonJsonParser parser = new GsonJsonParser();

	private static String removePrefix(String uri) {
		return uri.replace("/api/v1/", "");
	}

	public static String generatePath(String uri, Long lastId) {
		StringBuilder path = new StringBuilder();
		uri = removePrefix(uri);
		String[] split = uri.split("/");

		for (int i = 0; i < split.length; i++) {
			if (i % 2 == 0) {
				path.append(Character.toUpperCase(split[i].charAt(0)));
			} else {
				path.append(split[i]);
				path.append("/");
			}
		}
		path.append(lastId);

		return path.toString();
	}

	public static String generateEstimatedCurrentPath(String uri) {
		StringBuilder path = new StringBuilder();
		uri = removePrefix(uri);
		String[] split = uri.split("/");

		for (int i = 0; i < split.length; i++) {
			if (i % 2 == 0) {
				path.append(Character.toUpperCase(split[i].charAt(0)));
			} else {
				path.append(split[i]);
				if (i < split.length - 1) {
					path.append("/");
				}
			}
		}

		return path.toString();
	}

	public static String generateEstimatedParentPath(String uri, Long parentId) {
		uri = uri.substring(0, uri.lastIndexOf('/'));
		String parentUri = uri.substring(0, uri.lastIndexOf('/'));

		return generatePath(parentUri, parentId);
	}

	public static void checkIsValidJsonSchema(String schemaString) {
		if (schemaString == null) {
			return;
		}

		//FIXME 추후 JSON Schema 자체에 대한 유효성 검사 방법을 찾아서 추가
		try {
			parser.parseMap(schemaString);
		} catch (Exception e) {
			throw new InvalidApiInputException(INVALID_JSON.getMessage());
		}
	}
}
