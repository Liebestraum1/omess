import axios from "axios";

const chatClient = axios.create(
    {
        baseURL: 'http://localhost:8081',
        withCredentials: true,
        headers: {
            "Content-Type": 'application/json'
        }
    }
)
export const chatCreate = (projectId: number, data: string) => {
    return chatClient.post(`/api/v1/chat/${projectId}`, data);
}

export const getChatList = (projectId: number) => {
    return chatClient.get(`/api/v1/chat/${projectId}`);
}

export const leaveChat = async (projectId: number, chatId: string) => {
    return chatClient.delete(`/api/v1/chat/${projectId}/${chatId}`)
}