import { Type } from "../../types/common";
import { get } from "../common";

export const loadChatList = async (projectId: number) => {
    return get<Type[]>(`/api/v1/chat/${projectId}`);
};
