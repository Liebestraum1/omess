import {LabelProp} from "../Label/Label.ts";
import {MemberProp} from "../Member/Member.ts";

export type IssueProp = {
    issueId: number;
    title: string;
    charger: MemberProp;
    label: LabelProp;
    importance: number;
    status: number
}