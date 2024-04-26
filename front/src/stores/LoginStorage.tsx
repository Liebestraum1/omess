import { create } from "zustand";
import { LoginStatusProps } from "../types/Login/LoginProps";

type LoginStore = {
    loginStatus: LoginStatusProps;
    setServerLogin: () => void;
    setUserLogin: () => void;
};

export const useLoginStore = create<LoginStore>((set) => ({
    loginStatus: "none",

    setServerLogin: () =>
        set(() => ({
            loginStatus: "server",
        })),

    setUserLogin: () =>
        set(() => ({
            loginStatus: "user",
        })),
}));
