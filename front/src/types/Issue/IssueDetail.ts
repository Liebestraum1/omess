import {MemberProp} from "../Member/Member.ts";
import {LabelProp} from "../Label/Label.ts";

export type IssueDetailProp = {
    issueId: number;
    title: string;
    content: string;
    charger: MemberProp;
    label: LabelProp;
    importance: number;
    status: number
}