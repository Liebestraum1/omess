import { create } from "zustand";
import { ChatMessage } from "../types/chat/chat.ts";

type State = {
    chatId: string | null;
    messages: Array<ChatMessage> | null;
};

type Action = {
    setChatId: (value: string) => void;
    setMessages: (value: Array<ChatMessage>) => void;
    addMessage: (value: ChatMessage) => void;
    getMessage: (messageId: string) => ChatMessage | null;
    clear: () => void;
};

type ChatStore = State & Action;

export const useChat = create<ChatStore>((set, get) => {
    return {
        chatId: null,
        messages: null,
        setChatId: (chatId: string) => {
            set({ chatId: chatId });
        },
        setMessages: (messages: Array<ChatMessage>) => {
            set({ messages: { ...messages } });
        },
        addMessage: (message: ChatMessage) => {
            set((state) => ({
                messages: state.messages ? [...state.messages, message] : [message],
            }));
        },
        getMessage: (messageId: string) => {
            const messages = get().messages;
            return messages ? messages.filter((message) => message.messageId === messageId)[0] : null;
        },
        clear: () => set({ chatId: null, messages: null }),
    };
});
