import client from "../common";

export type SignInRequest = {
    email: string;
    password: string;
};

export type SignInResponse = {
    memberId: number;
    nickname: string;
};

export const signInApi = async <T = SignInResponse>(signInRequest: SignInRequest): Promise<T> => {
    const { data } = await client.post<T>("/api/v1/members/signin", signInRequest);
    return data;
};
