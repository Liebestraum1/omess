import axios from "axios";

export type SignUpRequest = {
    email: string;
    password: string;
    nickname: string;
};

export type SignUpResponse = {
    responseData: object;
    responseStatus: number;
    responseStatusText: string;
};

export const signUpApi = async <T = SignUpResponse>(signUpRequest: SignUpRequest): Promise<T> => {
    const { data } = await axios.post<T>("/api/v1/members/signup", signUpRequest);
    return data;
};

export const emailValidationApi = async <T>(email: string): Promise<T> => {
    const { data } = await axios.get<T>("/api/v1/members/check-email", { params: { email } });
    return data;
};

export const nicknameValidationApi = async <T>(nickname: string): Promise<T> => {
    const { data } = await axios.get<T>("/api/v1/members/check-nickname", { params: { nickname } });
    return data;
};
