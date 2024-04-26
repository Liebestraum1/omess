package org.sixback.omess.domain.apispecification.util;

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
}
