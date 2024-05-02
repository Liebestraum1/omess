export type ChatInfo = {
    id: string,
    name: string,
    notice?: ChatMessage,
    header?: ChatMessage,
    memberCount: number,
    messageCount: number
}

export type Writer = {
    id: number,
    email: string,
    nickname: string,
}

export type ChatMessage = {
    member: Writer,
    id: string,
    message: string,
    createAt: string,
    isUpdated: boolean
}