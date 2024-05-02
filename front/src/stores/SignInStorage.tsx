import { create } from "zustand";
import { SignInStatus } from "../types/SignIn/SignIn";

type SignInStore = {
    signInStatus: SignInStatus;
    memberId: number | undefined;
    memberNickname: string | undefined;
    setServerSignIn: () => void;
    setMemberSignIn: (memberId: number, memberNickname: string) => void;
};

export const useSignInStore = create<SignInStore>((set) => ({
    signInStatus: import.meta.env.VITE_APPLICATION_TYPE === "server" ? "server" : "none",
    memberNickname: undefined,
    memberId: undefined,

    setServerSignIn: () =>
        set(() => ({
            signInStatus: "server",
        })),

    setMemberSignIn: (memberId: number, memberNickname: string) => {
        set(() => ({
            signInStatus: "user",
            memberId: memberId,
            memberNickname: memberNickname,
        }));
    },
}));
