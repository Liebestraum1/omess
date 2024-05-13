import client from "../common";

export type Member = {
    id: number;
    nickname: string;
    email: string;
    profile: string;
};

export type CreateProjectRequest = {
    name: string | undefined;
};

export const getProjectApi = async <T>(): Promise<T> => {
    const { data } = await client.get<T>("/api/v1/projects");
    return data;
};

export const createProjectApi = async <T>(createProjectRequest: CreateProjectRequest): Promise<T> => {
    const { data } = await client.post<T>("/api/v1/projects", createProjectRequest);
    return data;
};

export const getMemberApi = async <T>(): Promise<T> => {
    const { data } = await client.get<T>("/api/v1/members");
    return data;
};
