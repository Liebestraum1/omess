export type CreateIssueProp = {
    title: string;
    content: string | null;
    importance: number;
    status: number;
    memberId: number | null;
    labelId: number | null;
}