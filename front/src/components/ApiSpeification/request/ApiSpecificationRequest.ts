import axios from "axios";
import {
    Api,
    ApiSpecification,
    PathVariable,
    QueryParam,
    RequestHeader
} from "../../../types/api-specification/ApiSpecification.ts";

export const loadApiSpecification = async <T = ApiSpecification>(projectId: number, apiSpecificationId: number): Promise<T> => {
    const {data} = await axios.get<T>(`/api/v1/projects/${projectId}/api-specifications/${apiSpecificationId}`);
    return data;
};

export const createDomain = async <T = void>(projectId: number, apiSpecificationId: number, newDomain: string): Promise<T> => {
    const {data} = await axios.post<T>(`/api/v1/projects/${projectId}/api-specifications/${apiSpecificationId}/domains`,
        {
            name: newDomain
        }
    );
    return data;
};

export const updateDomain = async <T = void>(projectId: number, apiSpecificationId: number, domainId: number, name: string): Promise<T> => {
    const {data} = await axios.patch<T>(`/api/v1/projects/${projectId}/api-specifications/${apiSpecificationId}/domains/${domainId}`,
        {
            name: name
        }
    );
    return data;
};

export const deleteDomain = async <T = void>(projectId: number, apiSpecificationId: number, domainId: number): Promise<T> => {
    const {data} = await axios.delete<T>(`/api/v1/projects/${projectId}/api-specifications/${apiSpecificationId}/domains/${domainId}`);
    return data;
};

export const loadApi = async <T = Api>(projectId: number, apiSpecificationId: number, domainId: number, apiId: number): Promise<T> => {
    const {data} = await axios.get<T>(`/api/v1/projects/${projectId}/api-specifications/${apiSpecificationId}/domains/${domainId}/apis/${apiId}`);
    return data;
};

export const createApi = async <T = void>(projectId: number, apiSpecificationId: number, domainId: number,
                                          method: string, name: string, description: string, endpoint: string, statusCode: number, requestSchema: string, responseSchema: string,
                                          createRequestHeaderRequests: RequestHeader[], createQueryParamRequests: QueryParam[], createPathVariableRequests: PathVariable[]): Promise<T> => {
    const {data} = await axios.post<T>(`/api/v1/projects/${projectId}/api-specifications/${apiSpecificationId}/domains/${domainId}/apis`,
        {
            name: name,
            description: description,
            endpoint: endpoint,
            method: method,
            statusCode: statusCode,
            requestSchema: requestSchema,
            responseSchema: responseSchema,
            createRequestHeaderRequests: createRequestHeaderRequests,
            createPathVariableRequests: createPathVariableRequests,
            createQueryParamRequests: createQueryParamRequests
        }
        );
    return data;
};


export const updateApi = async <T = void>(
    projectId: number, apiSpecificationId: number, domainId: number, apiId: number,
    method: string, name: string, description: string, endpoint: string, statusCode: number, requestSchema: string, responseSchema: string,
    updateRequestHeaderRequests: RequestHeader[], updateQueryParamRequests: QueryParam[], updatePathVariableRequests: PathVariable[]
): Promise<T> => {
    const {data} = await axios.put<T>(`/api/v1/projects/${projectId}/api-specifications/${apiSpecificationId}/domains/${domainId}/apis/${apiId}`,
        {
            name: name,
            description: description,
            endpoint: endpoint,
            method: method,
            statusCode: statusCode,
            requestSchema: requestSchema,
            responseSchema: responseSchema,
            updateRequestHeaderRequests: updateRequestHeaderRequests,
            updatePathVariableRequests: updatePathVariableRequests,
            updateQueryParamRequests: updateQueryParamRequests
        }
    );
    return data;
};

export const deleteApi = async <T = void>(projectId: number, apiSpecificationId: number, domainId: number, apiId: number): Promise<T> => {
    const {data} = await axios.delete<T>(`/api/v1/projects/${projectId}/api-specifications/${apiSpecificationId}/domains/${domainId}/apis/${apiId}`);
    return data;
};