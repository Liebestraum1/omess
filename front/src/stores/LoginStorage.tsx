import { create } from "zustand";
import { LoginStatusProps } from "../types/Login/LoginProps";

type LoginStore = {
    loginStatus: LoginStatusProps;
    setServerLogin: () => void;
    setUserLogin: () => void;
};

export const useLoginStore = create<LoginStore>((set) => ({
    loginStatus: import.meta.env.VITE_APPLICATION_TYPE === "server" ? "server" : "none",

    setServerLogin: () =>
        set(() => ({
            loginStatus: "server",
        })),

    setUserLogin: () =>
        set(() => ({
            loginStatus: "user",
        })),
}));
