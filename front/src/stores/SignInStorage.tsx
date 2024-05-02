import { create } from "zustand";
import { SignInStatus } from "../types/SignIn/SignIn";
import { persist } from "zustand/middleware";

type SignInStore = {
    signInStatus: SignInStatus;
    memberId: number | undefined;
    memberNickname: string | undefined;
    setServerSignIn: () => void;
    setMemberSignIn: (memberId: number, memberNickname: string) => void;
};

// TODO: LocalStorage 저장시 암호화 적용
export const useSignInStore = create(
    persist<SignInStore>(
        (set) => ({
            signInStatus: import.meta.env.VITE_APPLICATION_TYPE === "server" ? "server" : "none",
            memberNickname: undefined,
            memberId: undefined,

            setServerSignIn: () =>
                set(() => ({
                    signInStatus: "server",
                })),

            setMemberSignIn: (memberId: number, memberNickname: string) => {
                set(() => ({
                    signInStatus: "member",
                    memberId: memberId,
                    memberNickname: memberNickname,
                }));
            },
        }),
        {
            name: "login-storage",
        }
    )
);
