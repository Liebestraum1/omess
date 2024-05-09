import {create} from "zustand";
import {LabelProp} from "../types/Label/Label.ts";
import {MemberProp} from "../types/Member/Member.ts";
import {IssueProp} from "../types/Issue/Issue.ts";
import {CreateIssueProp} from "../types/Issue/CreateIssue.ts";
import axios from "axios";
import {KanbanBoardProp} from "../types/KanbanBoard/KanbanBoard.ts";
import {IssueDetailProp} from "../types/Issue/IssueDetail.ts";
import {UpdateIssueRequest} from "../types/Issue/UpdateIssueRequest.ts";
import {CompatClient} from "@stomp/stompjs";

type KanbanBoardStorage = {
    // 전역 변수
    kanbanBoardId: number | null;
    issueId: number | null;
    clent: CompatClient | null;
    issues: IssueProp[];
    labels: LabelProp[];
    projectMembers: MemberProp[];
    selectedLabel: number | null;
    selectedMember: number | null;
    selectedImpotance: number | null;

    // 전역 변수 setter
    setKanbanBoardId: (kanbanBoardId: number) => void;
    setIssuedId: (issueId: number | null) => void;
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
    getIssueDetail: (projectId: number, moduleId: number, issueId: number) => Promise<IssueDetailProp>;
    updateIssueLabel: (projectId: number, moduleId: number, issueId: number, labelId: number) => void;
    updateIssueStatus: (projectId: number, moduleId: number, issueId: number, status: number) => void;
    updateIssueImportance: (projectId: number, moduleId: number, issueId: number, importance: number) => void;
    updateIssueMember: (projectId: number, moduleId: number, issueId: number, chargerId: number) => void;
    updateIssue: (projectId: number, moduleId: number, issueId: number, updateIssueRequest: UpdateIssueRequest) => void;
    deleteIssue: (projectId: number, moduleId: number, issueId: number) => void;
}

export const useKanbanBoardStore = create<KanbanBoardStorage>((set, get) => ({
    // 전역 변수
    kanbanBoardId: null,
    issueId: null,
    clent: null,
    issues: [],
    labels: [],
    projectMembers: [],
    selectedLabel: null,
    selectedMember: null,
    selectedImpotance: null,

    // 전역 변수 setter
    setKanbanBoardId: (kanbanBoardId: number) => set({kanbanBoardId}),
    setIssuedId: (issueId: number | null) => set({issueId}),
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
        await axios.get(`/api/v1/projects/${projectId}/kanbanboards/${moduleId}`)
            .then((reponse) => {
                const kanbanBoard: KanbanBoardProp = reponse.data;
                get().setIssues(kanbanBoard.issues);
            });

    },
    getIssues: async (projectId: number, moduleId: number, chargerId: number | null, labelId: number | null, importance: number | null) => {
        await axios.get(`/api/v1/projects/${projectId}/kanbanboards/${moduleId}/issues`,
            {
                params: {
                    chargerId: chargerId,
                    labelId: labelId,
                    importance: importance
                }
            }
        ).then((reponse) => {
            const issues = reponse.data;

            get().setIssues(issues.issues);

        });
    },
    getLabels: async (projectId: number, moduleId: number) => {
        await axios.get(`/api/v1/projects/${projectId}/kanbanboards/${moduleId}/label`)
            .then((response) => {
                const labels = response.data;
                get().setLabels(labels.labels);
            });
    },

    createIssue: async (projectId: number, moduleId: number, writeIssueRequest: CreateIssueProp) => {
        await axios.post(`/api/v1/projects/${projectId}/kanbanboards/${moduleId}/label`,
            {
                writeIssueRequest: writeIssueRequest
            }
        ).then(() => {
            get().getIssues(projectId, moduleId, get().selectedMember, get().selectedLabel, get().selectedImpotance);
        });
    },

    getIssueDetail: async (projectId: number, moduleId: number, issueId: number): Promise<IssueDetailProp> => {
        try {
            const response = await axios.get(`/api/v1/projects/${projectId}/kanbanboards/${moduleId}/issues/${issueId}`);
            return response.data;
        } catch (error) {
            console.error("Failed to fetch issue details: ", error);
            throw error;
        }
    },

    updateIssueLabel: async (projectId: number, moduleId: number, issueId: number, labelId: number) => {
        await axios.patch(`/api/v1/projects/${projectId}/kanbanboards/${moduleId}/issues/${issueId}/label`,
            {
                labelId: labelId
            }
        )
    },

    updateIssueStatus: async (projectId: number, moduleId: number, issueId: number, status: number) => {
        await axios.patch(`/api/v1/projects/${projectId}/kanbanboards/${moduleId}/issues/${issueId}/status`,
            {
                status: status
            }
        )
    },

    updateIssueImportance: async (projectId: number, moduleId: number, issueId: number, importance: number) => {
        await axios.patch(`/api/v1/projects/${projectId}/kanbanboards/${moduleId}/issues/${issueId}/importance`,
            {
                importance: importance
            }
        )
    },

    updateIssueMember: async (projectId: number, moduleId: number, issueId: number, chargerId: number) => {
        await axios.patch(`/api/v1/projects/${projectId}/kanbanboards/${moduleId}/issues/${issueId}/member`,
            {
                chargerId: chargerId
            }
        )
    },

    updateIssue: async (projectId: number, moduleId: number, issueId: number, updateIssueRequest: UpdateIssueRequest) => {
        await axios.patch(`/api/v1/projects/${projectId}/kanbanboards/${moduleId}/issues/${issueId}`,
            updateIssueRequest
        )
    },

    deleteIssue: async (projectId: number, moduleId: number, issueId: number) => {
        await axios.delete(`/api/v1/projects/${projectId}/kanbanboards/${moduleId}/issues/${issueId}`)
    },
}));