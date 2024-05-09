import client from "../common";

export type ModuleResponse = {
    id: number;
    title: string;
    category: string;
};

export type ModuleCategoryResponse = {
    id: number;
    category: string;
    path: string;
};

export type ModuleCreateRequest = {
    name: string;
    category: string;
};

export const getModulesApi = async <T>(projectId: number): Promise<T> => {
    const { data } = await client.get<T>(`/api/v1/projects/${projectId}/modules`);
    return data;
};

export const getModuleCategoryApi = async <T>(projectId: number): Promise<T> => {
    const { data } = await client.get<T>(`/api/v1/projects/${projectId}/modules/modulecategory`);
    return data;
};

export const createModuleApi = async <T>(
    projectId: number,
    path: string,
    moduleCreateRequest: ModuleCreateRequest
): Promise<T> => {
    const { data } = await client.post<T>(`/api/v1/projects/${projectId}/${path}`, moduleCreateRequest);

    return data;
};
