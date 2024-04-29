import {get} from "../common";
import {Type} from "../../types/common";

export const loadChatList = async (projectId: number) => {
    return get<Type[]>(`/api/v1/chat/${projectId}`);
}
