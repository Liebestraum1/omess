export type ChatInfo = {
    id: string,
    name: string,
    header?: string,
}

export type Header = {
    detail: string,
    message: ChatMessage
}

export type ChatName = {
    chatName: string,
    message: ChatMessage
}

export type Writer = {
    id: number,
    email: string,
    nickname: string,
}

export type ChatMessage = {
    classify: string,
    member: Writer,
    id: string,
    message: string,
    createAt: string,
    isUpdated: boolean,
    isPined: boolean
}