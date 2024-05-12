export type ChatInfo = {
    id: string,
    name: string,
    header: string,
    memberCount: number,
    pinCount: number,
}

export type Header = {
    detail: string,
    message: ChatMessage
}

export type ChatName = {
    chatName: string,
    message: ChatMessage
}

export type ChatMember = {
    id: number,
    email: string,
    nickname: string,
    profile: string | undefined,
    role: string
}

export type ChatMessage = {
    classify: string,
    member: ChatMember,
    id: string,
    message: string,
    createAt: string,
    isUpdated: boolean,
    isPined: boolean,
    files: Array<FileInformation>
}

export type FileInformation = {
    id: number,
    address: string
}