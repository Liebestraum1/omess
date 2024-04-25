export type ChatInfo = {
    id: string,
    name: string,
    messageCount: number
}

export type ChatSubscriber = {
    email: string,
    nickname: string,
    profile: string
}

export type ChatMessage = {
    messageId: string,
    messageType: string,
    writer: ChatSubscriber,
    message: string,
    time: string,
    isModified: boolean
}

export type DeletedMessage = {
    messageId: string
}

export type RoleChange = {
    target: ChatSubscriber,
    to: string
}