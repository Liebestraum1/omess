import {create} from "zustand";
import {ChatInfo, ChatMessage, Writer} from "../types/chat/chat.ts";

type ChatStorage = {
    chatId: string | null;
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
        if (messages == null) {
            set({messages: [message]})
        } else {
            messages.push(message);
            set({messages: messages})
        }
    }
    const addFirst = (message: ChatMessage) => {
        const messages = get().messages;
        if (messages == null) {
            set({messages: [message]})
        } else {
            messages.unshift(message);
            set({messages: messages})
        }
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

    // const addMember = (writer: Writer) => {
    //     const members = get().members;
    //     if (members == null) {
    //         set({members: [writer]})
    //     } else {
    //         get().members!.push(writer)
    //         set({members: members})
    //     }
    // };

    return {
        serverUrl: 'ws://localhost:8081/chat/v1',
        chatId: null,
        client: null,
        members: null,
        messages: null,
        init: (chat: ChatInfo) => {
            if (chat == null) {
                console.log('must have chatInfo')
                return;
            }

            const url = get().serverUrl
            const client = new WebSocket(url)
            set({client, chatId: chat.id})

            client.onopen = () => {
                const enter = {
                    type: 'ENTER',
                    data: {
                        memberId: 1,
                        chatId: `${get().chatId}`
                    }
                };
                client.send(JSON.stringify(enter));
            }

            client.onerror = (error) => {
                console.error("에러발생!!!", error);
            };

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
        }
    }
});