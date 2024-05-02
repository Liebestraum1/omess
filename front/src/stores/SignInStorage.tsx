import { create } from "zustand";
import { SignInStatus } from "../types/SignIn/SignIn";
import { PersistStorage, StorageValue, persist } from "zustand/middleware";
import CryptoJS from "crypto-js";

const secretKey = import.meta.env.VITE_SECRET_KEY;

const encrypt = (payload: string): string => {
    const encrypted = CryptoJS.AES.encrypt(payload, secretKey).toString();
    return encrypted;
};

const decrypt = (encryptedPayload: string): StorageValue<SignInStore> => {
    const decryptedBytes = CryptoJS.AES.decrypt(encryptedPayload, secretKey);
    const decrypted = decryptedBytes.toString(CryptoJS.enc.Utf8);
    const decryptedObject = JSON.parse(decrypted);

    return decryptedObject as StorageValue<SignInStore>;
};

type SignInStore = {
    signInStatus: SignInStatus;
    memberId: number | undefined;
    memberNickname: string | undefined;
    setServerSignIn: () => void;
    setMemberSignIn: (memberId: number, memberNickname: string) => void;
};

const signInStorage: PersistStorage<SignInStore> = {
    getItem: (name) => {
        const value = localStorage.getItem(name);
        if (!value) return null;
        return decrypt(value);
    },

    setItem: (name, value: StorageValue<SignInStore>) => {
        const stringValue = JSON.stringify(value);
        const encryptedValue = encrypt(stringValue);
        localStorage.setItem(name, encryptedValue);
    },
    removeItem: (name) => localStorage.removeItem(name),
};

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
            name: "signin",
            storage: signInStorage,
        }
    )
);
