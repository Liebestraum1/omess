import { create } from "zustand";
import { SignInStatus } from "../types/SignIn/SignIn";

type SignInStore = {
    signInStatus: SignInStatus;
    setServerSignIn: () => void;
    setUserSignIn: () => void;
};

export const useSignInStore = create<SignInStore>((set) => ({
    signInStatus: import.meta.env.VITE_APPLICATION_TYPE === "server" ? "server" : "none",

    setServerSignIn: () =>
        set(() => ({
            signInStatus: "server",
        })),

    setUserSignIn: () =>
        set(() => ({
            signInStatus: "user",
        })),
}));
