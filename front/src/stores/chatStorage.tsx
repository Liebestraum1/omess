import {create} from "zustand";
import {ChatInfo, ChatMessage, ChatName, Header, Writer} from "../types/chat/chat.ts";

type ChatStorage = {
    chatId: string | null;
    chatInfo: ChatInfo | null,
    serverUrl: string;
    client: WebSocket | null;
    members: Writer[] | null;
    messages: Array<ChatMessage> | null;
    init: (chat: ChatInfo) => void;
    sendMessage: (data: any) => void;
};

export const useChatStorage = create<ChatStorage>((set, get) => {
    const addLast = (message: ChatMessage) => {
        const messages = get().messages;
        messages!.push(message);
        set({messages: messages})
    }
    const addFirst = (message: ChatMessage) => {
        const messages = get().messages;
        messages!.unshift(message);
        set({messages: messages})
    }

    const updateMessage = (message: ChatMessage) => {
        const messages = get().messages;

        const updatedMessages = messages!.map(msg =>
            msg.id === message.id ? {...msg, message: message.message, isUpdated: message.isUpdated} : msg
        );

        set({messages: updatedMessages});
    }

    const deleteMessage = (message: ChatMessage) => {
        const messages = get().messages;

        const deleteMessages = messages!.filter(msg =>
            msg.id !== message.id
        )

        set({messages: deleteMessages});
    }

    const pinMessage = (message: ChatMessage) => {
        const messages = get().messages;

        const updatedMessages = messages!.map(msg =>
            msg.id === message.id ? {...msg, isPined: message.isPined} : msg
        );

        set({messages: updatedMessages});
    }

    const headerMessage = (header: Header) => {
        const chat = get().chatInfo;

        chat!.header = header.detail

        addFirst(header.message);
        set({chatInfo: chat});
    }

    const chatNameMessage = (chatName: ChatName) => {
        const chat = get().chatInfo;
        chat!.name = chatName.chatName

        set({chatInfo: chat})
    }

    return {
        serverUrl: 'ws://localhost:8081/chat/v1',
        chatId: null,
        client: null,
        members: null,
        messages: null,
        chatInfo: null,
        init: (chat: ChatInfo) => {
            if (chat == null) {
                console.log('must have chatInfo')
                return;
            }

            set({chatInfo: chat, chatId: chat.id})

            const url = get().serverUrl
            let client = get().client;
            if (client == null) {
                client = new WebSocket(url)
                set({client})
            }

            client.onopen = () => {
                if (client.readyState === WebSocket.OPEN) {
                    const enter = {
                        type: 'ENTER',
                        data: {
                            memberId: 1,
                            chatId: `${get().chatId}`
                        }
                    };
                    client.send(JSON.stringify(enter));
                    set({messages: []})
                }
            }

            client.onerror = (error) => {
                console.error("에러발생!!!", error);
                set({client: null})
            };

            client.onclose = () => {
                set({client: null})
            }

            client.onmessage = (event) => {
                const message = JSON.parse(event.data);
                console.log(message.type, message.data)
                switch (message.type) {
                    case "MESSAGE":
                        addFirst(message.data);
                        break;
                    case "HISTORY":
                        addLast(message.data);
                        break;
                    case "UPDATE":
                        updateMessage(message.data);
                        break;
                    case "DELETE":
                        deleteMessage(message.data);
                        break;
                    case "PIN":
                        pinMessage(message.data);
                        break
                    case "HEADER":
                        headerMessage(message.data);
                        break
                    case "CHAT_NAME":
                        chatNameMessage(message.data);
                        break
                    case "SUCCESS":
                        console.log("연결 성공")
                        break;
                }
            };
        },
        sendMessage: (data: any) => {
            if (get().client == null) {
                console.error('client 연결 안돼 있음')
            } else {
                get().client!.send(data)
            }
        },
    }
});