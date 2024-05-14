import {create} from "zustand";
import {ChatInfo, ChatMember, ChatMessage, ChatName, FileInformation, Header} from "../types/chat/chat.ts";

type ChatStorage = {
    chatList: Array<ChatInfo> | null,
    chatId: string | null;
    chatInfo: ChatInfo | null,
    setChat: (chat: ChatInfo) => void;
    serverUrl: string;
    client: WebSocket | null;
    members: Array<ChatMember> | null;
    messages: Array<ChatMessage> | null;
    isEnter: boolean;
    files: Array<FileInformation> | null;
    pinMessages: Array<ChatMessage> | null;
    init: (memberId: number) => void;
    sendMessage: (data: any) => void;
    addFile: (filename: FileInformation) => void;
    removeFile: (fileId: number) => void;
    resetFile: () => void;
    setChatList: (chatList: Array<ChatInfo>) => void;
    addChat: (chat: ChatInfo) => void;
    removeChat: (chatId: string) => void;
    reset: () => void;
    enter: (memberId: number) => void;
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
        const chat = get().chatInfo!;
        const messages = get().messages;

        chat.pinCount = message.isPined ? chat.pinCount - 1 : chat.pinCount;
        const deleteMessages = messages!.filter(msg =>
            msg.id !== message.id
        )

        set({chatInfo: chat, messages: deleteMessages});
    }

    const pinMessage = (message: ChatMessage) => {
        const messages = get().messages;
        const chat = get().chatInfo!

        chat.pinCount = message.isPined ? chat.pinCount + 1 : chat.pinCount - 1;
        const updatedMessages = messages!.map(msg =>
            msg.id === message.id ? {...msg, isPined: message.isPined} : msg
        );

        set({chatInfo: chat, messages: updatedMessages});
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
        addFirst(chatName.message)
    }

    const membersMessage = (member: any) => {
        const members = get().members;
        members!.push({...member.member, ...member});
        set({members: members});
    }

    const loadPinMessage = (message: ChatMessage) => {
        const pinMessages = get().pinMessages;
        pinMessages!.push(message);
        set({pinMessages: pinMessages});
    }

    return {
        chatList: [],
        serverUrl: import.meta.env.VITE_WEBSOCKET_URL,
        isEnter: false,
        chatId: null,
        client: null,
        members: null,
        messages: null,
        chatInfo: null,
        pinMessages: null,
        files: null,
        init: (memberId: number) => {
            let client = get().client;
            const chat = get().chatInfo
            if (chat == null) {
                return;
            }

            const url = get().serverUrl
            client = new WebSocket(url)
            set({client})

            client.onopen = () => {
                if (client!.readyState === WebSocket.OPEN) {
                    const enter = {
                        type: 'ENTER',
                        data: {
                            memberId: memberId,
                            chatId: `${get().chatId}`
                        }
                    };
                    client!.send(JSON.stringify(enter));
                    set({messages: [], pinMessages: [], members: [], files: []})
                }
            }

            client.onerror = () => {
                set({client: null})
            };

            client.onclose = () => {
                set({client: null})
            }

            client.onmessage = (event) => {
                if (client!.readyState !== WebSocket.OPEN) return;
                const message = JSON.parse(event.data);
                // console.log(message.type, message.data)
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
                    case "MEMBERS":
                        membersMessage(message.data);
                        break
                    case "LOAD_PIN":
                        loadPinMessage(message.data);
                        break;
                    case "SUCCESS":
                        set({isEnter: true})
                        console.log("연결 성공")
                        break;
                }
            };
        },
        enter: (memberId: number) => {
            if (get().client!.readyState !== WebSocket.OPEN) return;
            const enter = {
                type: 'ENTER',
                data: {
                    memberId: memberId,
                    chatId: `${get().chatId}`
                }
            };
            get().client!.send(JSON.stringify(enter));
            set({messages: [], pinMessages: [], members: [], files: [], isEnter: false})
        },
        sendMessage: (data: any) => {
            if (get().client == null || get().client!.readyState !== WebSocket.OPEN) {
                console.error('no client')
            } else {
                get().client!.send(data)
            }
        },
        addFile: (file: FileInformation) => {
            const files = get().files;
            files!.push(file)
            set({files: files})
        },
        removeFile: (fileId: number) => {
            const files = get().files;
            const res = files!.filter(value => value.id !== fileId);
            set({files: res});
        },
        resetFile: () => {
            set({files: []})
        },
        setChatList: (chatList: Array<ChatInfo>) => {
            set({chatList: chatList})
        },
        removeChat: (chatId: string) => {
            const chatList = get().chatList?.filter(value => value.id !== chatId);
            set({chatList: chatList})
        },
        addChat: (chat: ChatInfo) => {
            const chatList = get().chatList ?? [];
            chatList.push(chat)
            set({chatList: chatList})
        },
        setChat: (chat: ChatInfo) => {
            set({chatId: chat.id, chatInfo: chat})
        },
        reset: () => {
            set({
                messages: null,
                files: null,
                members: null,
                pinMessages: null,
                client: null
            })
        }
    }
});