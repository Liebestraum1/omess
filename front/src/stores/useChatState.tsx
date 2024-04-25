import {atom, selectorFamily, useRecoilState} from "recoil";
import {ChatInfo, ChatMessage, ChatSubscriber} from "../types/chat/chat.ts";

// Chat 상태 정의
const chatInfoState = atom<ChatInfo | null>({
    key: 'chatInfoState',
    default: null,
});

const chatMembersState = atom<ChatSubscriber[] | null>({
    key: 'chatMembersState',
    default: null,
});

const messagesState = atom<ChatMessage[] | null>({
    key: 'messagesState',
    default: null,
});

export const filteredMessageState = selectorFamily<ChatMessage[] | null, string>({
    key: 'filteredMessage',
    get: (messageId: string) => ({get}) => {
        const list = get(messagesState);
        if (list === null) {
            return [];
        }
        return list.filter(message => message.messageId === messageId);
    }
})

export const filteredMemberState = selectorFamily<ChatSubscriber[] | null, string>({
    key: 'filteredMember',
    get: (email: string) => ({get}) => {
        const list = get(chatMembersState);
        if (list === null) {
            return []
        }
        return list.filter(member => member.email === email)
    }
})


export function UseChatInfo() {
    return useRecoilState(chatInfoState);
}

export function UseChatMembersState() {
    return useRecoilState(chatMembersState);
}
export function UseMessagesState() {
    return useRecoilState(messagesState);
}
