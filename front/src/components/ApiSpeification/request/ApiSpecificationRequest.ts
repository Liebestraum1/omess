
import axios from "axios";
import {ApiSpecification} from "../../../types/api-specification/ApiSpecification.ts";

export const loadApiSpecification = async <T = ApiSpecification>(projectId: number, apiSpecificationId: number): Promise<T> => {
    const { data } = await axios.get<T>(`/api/v1/projects/${projectId}/api-specifications/${apiSpecificationId}`);
    return data;
};