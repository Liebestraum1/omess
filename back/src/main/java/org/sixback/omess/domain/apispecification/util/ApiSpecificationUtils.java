package org.sixback.omess.domain.apispecification.util;

import static org.sixback.omess.domain.apispecification.exception.ApiSpecificationErrorMessage.*;

import org.sixback.omess.domain.apispecification.exception.InvalidJsonSchemaException;
import org.springframework.boot.json.GsonJsonParser;

public class ApiSpecificationUtils {
    public static String generatePath(String uri, Long lastId){
        StringBuilder path = new StringBuilder();
        uri = uri.replace("/api/v1/", "");
        String[] split = uri.split("/");

        for (int i = 0; i < split.length; i++) {
            if(i % 2 == 0){
                path.append(Character.toUpperCase(split[i].charAt(0)));
            }else{
                path.append(split[i]);
                path.append("/");
            }
        }
        path.append(lastId);

        return path.toString();
    }

    public static String generateEstimatedParentPath(String uri, Long parentId){
        uri = uri.substring(0, uri.lastIndexOf('/'));
        String parentUri = uri.substring(0, uri.lastIndexOf('/'));

        return generatePath(parentUri, parentId);
    }

    public static void checkIsValidJsonSchema(String schemaString){
        if(schemaString == null){
            return;
        }
        GsonJsonParser parser = new GsonJsonParser();
        //FIXME 추후 JSON Schema 자체에 대한 유효성 검사 방법을 찾아서 추가
        try {
            parser.parseMap(schemaString);
        } catch (Exception e) {
            throw new InvalidJsonSchemaException(INVALID_JSON.getMessage());
        }
    }
}
