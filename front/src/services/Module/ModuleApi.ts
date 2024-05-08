import client from "../common";

export type ModuleResponse = {
    id: number;
    title: string;
    category: string;
};

export const getModulesApi = async <T>(projectId: number): Promise<T> => {
    const { data } = await client.get<T>(`/api/v1/projects/${projectId}/modules`);
    return data;
};
