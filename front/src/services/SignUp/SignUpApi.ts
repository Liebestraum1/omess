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

export const signUpApi = async (signUpRequest: SignUpRequest): Promise<SignUpResponse> => {
    const response = await axios.post("/api/v1/members/signup", signUpRequest);
    return {
        responseData: response.data,
        responseStatus: response.status,
        responseStatusText: response.statusText,
    };
};
