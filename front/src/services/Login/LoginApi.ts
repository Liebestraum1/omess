import axios from "axios";

export type SignInRequest = {
    email: string;
    password: string;
    nickname: string;
};

export type SignInResponse = {
    responseData: object;
    responseStatus: number;
    responseStatusText: string;
};

export const signInApi = async (signInRequest: SignInRequest): Promise<SignInResponse> => {
    // async => Promise<pending | fulfilled | rejected>
    // awiat을 안 걸면 => result는 아마 대부분의 경우 Promise<pending> 상태일 것이다.
    // awiat을 걸면 => result가 resolve 될 때까지 기다리고, 응답을 받으면 fulfilled로 변환됨

    // 그 때는 Promise<testRequest>가 확실히 반환된다.
    // await은 resolve될때까지 함수를 일시정지시킨다
    // 하지만 일시정지와 별개로 getHello는 무조건 Promise를 반환한다 (pending이건 fulfill이건..)

    const response = await axios.post("/api/v1/members/signup", signInRequest);
    return {
        responseData: response.data,
        responseStatus: response.status,
        responseStatusText: response.statusText,
    };
};
