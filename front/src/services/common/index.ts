import axios from "axios";
// const baseURL = "http://localhost:8080"

// const client = axios.create({
//     baseURL
// });

// export const get = async<T> (url: string): Promise<T> => {
//     const {data} = await client.get<T>(url);
//     return data;
// }

// export default client;

import { useSignInStore } from "../../stores/SignInStorage";

axios.interceptors.response.use(
    (response) => {
        return response;
    },
    (error) => {
        if (error.response && error.response.status === 401) {
            useSignInStore.getState().setUserLogout();
        }
        return Promise.reject(error);
    }
);

export default axios;
