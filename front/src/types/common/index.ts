export type Type = {
    body: ResponseBody;
    headers: object;
    statusCode: string;
    statusCodeValue: number;
};
export type ResponseBody = {
    id: string;
    memberCount: number;
    messageCount: number;
    name: string;
    type: string;
};
