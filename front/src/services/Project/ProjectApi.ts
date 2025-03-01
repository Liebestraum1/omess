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

export type InviteProjectRequest = {
    inviteMembers: Array<number>;
};

export const getProjectApi = async <T>(): Promise<T> => {
    const { data } = await client.get<T>("/api/v1/projects");
    return data;
};

export const createProjectApi = async <T>(createProjectRequest: CreateProjectRequest): Promise<T> => {
    const { data } = await client.post<T>("/api/v1/projects", createProjectRequest);
    return data;
};

export const leaveProjectApi = async <T>(projectId: number): Promise<T> => {
    const { data } = await client.post<T>(`/api/v1/projects/${projectId}/leave`);
    return data;
};

export const getMemberApi = async <T>(): Promise<T> => {
    const { data } = await client.get<T>("/api/v1/members");
    return data;
};

export const inviteProjectApi = async <T>(
    projectId: number,
    InviteProjectRequest: InviteProjectRequest
): Promise<T> => {
    const { data } = await client.post<T>(`/api/v1/projects/${projectId}`, InviteProjectRequest);
    return data;
};
