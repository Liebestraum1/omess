import axios from "axios";

import { useSignInStore } from "../../stores/SignInStorage";

const client = axios;
client.interceptors.response.use(
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

export default client;
