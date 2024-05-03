
import axios from "axios";
import {ApiSpecification} from "../../../types/api-specification/ApiSpecification.ts";

export const loadApiSpecification = async <T = ApiSpecification>(projectId: number, apiSpecificationId: number): Promise<T> => {
    const { data } = await axios.get<T>(`/api/v1/projects/${projectId}/api-specifications/${apiSpecificationId}`);
    return data;
};

export const updateDomain = async <T = void>(projectId: number, apiSpecificationId: number,  domainId: number, name: string): Promise<T> => {
    const { data } = await axios.patch<T>(`/api/v1/projects/${projectId}/api-specifications/${apiSpecificationId}/domains/${domainId}`,
        {
            name: name
        }
    );
    return data;
};

export const deleteDomain = async <T = void>(projectId: number, apiSpecificationId: number,  domainId: number): Promise<T> => {
    const { data } = await axios.delete<T>(`/api/v1/projects/${projectId}/api-specifications/${apiSpecificationId}/domains/${domainId}`);
    return data;
};
