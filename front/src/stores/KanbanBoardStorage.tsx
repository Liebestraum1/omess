import {create} from "zustand";
import {LabelProp} from "../types/Label/Label.ts";
import {MemberProp} from "../types/Member/Member.ts";
import {IssueProp} from "../types/Issue/Issue.ts";
import {CreateIssueProp} from "../types/Issue/CreateIssue.ts";
import axios from "axios";
import {KanbanBoardProp} from "../types/KanbanBoard/KanbanBoard.ts";
import {IssueDetailProp} from "../types/Issue/IssueDetail.ts";

type KanbanBoardStorage = {
    // 전역 변수
    kanbanBoardId: number | null;
    issues: IssueProp[];
    labels: LabelProp[];
    projectMembers: MemberProp[];
    selectedLabel: number | null;
    selectedMember: number | null;
    selectedImpotance: number | null;

    // 전역 변수 setter
    setKanbanBoardId: (kanbanBoardId: number) => void;
    setIssues: (issues: IssueProp[]) => void;
    setLabels: (labels: LabelProp[]) => void;
    setprojectMembers: (projectMembers: MemberProp[]) => void;
    setSelectedLabel: (labelId: number | null, projectId: number, moduleId: number) => void;
    setSelectedMember: (memberId: number | null, projectId: number, moduleId: number) => void;
    setSelectedImpotance: (impotance: number | null, projectId: number, moduleId: number) => void;

    //api 요청
    getKanbanBoard: (projectId: number, moduleId: number) => void;
    getIssues: (projectId: number, moduleId: number, chargerId: number | null, labelId: number | null, importance: number | null) => void;
    getLabels: (projectId: number, moduleId: number) => void;
    createIssue: (projectId: number, moduleId: number, writeIssueRequest: CreateIssueProp) => void;
    getIssueDetail: (projectId: number, moduleId: number, issueId: number) =>  Promise<IssueDetailProp>;
}

export const useKanbanBoardStore = create<KanbanBoardStorage>((set, get) => ({
    // 전역 변수
    kanbanBoardId: null,
    issues: [],
    labels: [],
    projectMembers: [],
    selectedLabel: null,
    selectedMember: null,
    selectedImpotance: null,

    // 전역 변수 setter
    setKanbanBoardId: (kanbanBoardId: number) => set({kanbanBoardId}),
    setIssues: (issues: IssueProp[]) => set({issues}),
    setLabels: (labels: LabelProp[]) => set({labels}),
    setprojectMembers: (projectMembers: MemberProp[]) => set({projectMembers}),
    setSelectedLabel: (labelId: number | null, projectId: number, moduleId: number) => {
        set({selectedLabel: labelId});
        get().getIssues(projectId, moduleId, get().selectedMember, get().selectedLabel, get().selectedImpotance);
    },
    setSelectedMember: (memberId: number | null, projectId: number, moduleId: number) => {
        set({selectedMember: memberId});
        get().getIssues(projectId, moduleId, get().selectedMember, get().selectedLabel, get().selectedImpotance);
    },
    setSelectedImpotance: (impotance: number | null, projectId: number, moduleId: number) => {
        set({selectedImpotance: impotance});
        get().getIssues(projectId, moduleId, get().selectedMember, get().selectedLabel, get().selectedImpotance);
    },

    //api 요청
    getKanbanBoard: async (projectId: number, moduleId: number) => {
        await axios.get(`http://localhost:8080/api/v1/projects/${projectId}/kanbanboards/${moduleId}`)
            .then((reponse) => {
                const kanbanBoard: KanbanBoardProp = JSON.parse(reponse.data);

                get().setIssues(kanbanBoard.issues);
            });

    },
    getIssues: async (projectId: number, moduleId: number, chargerId: number | null, labelId: number | null, importance: number | null) => {
        await axios.get(`http://localhost:8080/api/v1/projects/${projectId}/kanbanboards/${moduleId}/issues`,
            {
                params: {
                    chargerId: chargerId,
                    labelId: labelId,
                    importance: importance
                }
            }
        ).then((reponse) => {
            const issues: IssueProp[] = JSON.parse(reponse.data);

            get().setIssues(issues);
        });
    },
    getLabels: async (projectId: number, moduleId: number) => {
        await axios.get(`http://localhost:8080/api/v1/projects/${projectId}/kanbanboards/${moduleId}/label`)
            .then((response) => {
                const labels: LabelProp[] = JSON.parse(response.data);

                get().setLabels(labels);
            });
    },

    createIssue: async (projectId: number, moduleId: number, writeIssueRequest: CreateIssueProp) => {
        await axios.post(`http://localhost:8080/api/v1/projects/${projectId}/kanbanboards/${moduleId}/label`,
            {
                writeIssueRequest: writeIssueRequest
            }
        ).then(() => {
            get().getIssues(projectId, moduleId, get().selectedMember, get().selectedLabel, get().selectedImpotance);
        });
    },

    getIssueDetail: async (projectId: number, moduleId: number, issueId: number): Promise<IssueDetailProp> => {
        try {
            const response = await axios.get<IssueDetailProp>(`http://localhost:8080/api/v1/projects/${projectId}/kanbanboards/${moduleId}/issue/${issueId}`);
            return response.data;
        } catch (error) {
            console.error("Failed to fetch issue details: ", error);
            throw error;
        }
    }
}));