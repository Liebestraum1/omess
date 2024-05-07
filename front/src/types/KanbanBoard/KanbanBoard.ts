import {IssueProp} from "../Issue/Issue.ts";

export type KanbanBoardProp = {
    moduleId: number;
    title: string;
    category: string;
    issues: IssueProp[];
}