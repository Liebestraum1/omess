import {create} from "zustand";
import {LabelProp} from "../types/Label/Label.ts";
import {MemberProp} from "../types/Member/Member.ts";
import {IssueProp} from "../types/Issue/Issue.ts";
import {CreateIssueProp} from "../types/Issue/CreateIssue.ts";
import client from "../services/common" ;
import {KanbanBoardProp} from "../types/KanbanBoard/KanbanBoard.ts";
import {IssueDetailProp} from "../types/Issue/IssueDetail.ts";
import {UpdateIssueRequest} from "../types/Issue/UpdateIssueRequest.ts";
import {CompatClient} from "@stomp/stompjs";

type KanbanBoardStorage = {
    // 전역 변수
    currentProjectId: number | null;
    kanbanBoardId: number | null;
    issueId: number | null;
    client: CompatClient | null;
    issues: IssueProp[];
    labels: LabelProp[];
    projectMembers: MemberProp[];
    selectedLabel: number | null;
    selectedMember: number | null;
    selectedImpotance: number | null;

    // 전역 변수 setter
    setCurrentProjectId: (projectId: number) => void;
    setKanbanBoardId: (kanbanBoardId: number) => void;
    setIssuedId: (issueId: number | null) => void;
    setClient: (client: CompatClient | null) => void;
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
    getMembersInproject: (projectId: number) => void;
    createLabel: (projectId: number, moduleId: number, name: string) => void;
    deleteLabel: (projectId: number, moduleId: number, labelId: number) => void;
    sendIssue: () => void;
    sendlabel: () => void;
}

export const useKanbanBoardStore = create<KanbanBoardStorage>((set, get) => ({
    // 전역 변수
    currentProjectId: null,
    kanbanBoardId: null,
    issueId: null,
    client: null,
    issues: [],
    labels: [],
    projectMembers: [],
    selectedLabel: null,
    selectedMember: null,
    selectedImpotance: null,

    // 전역 변수 setter
    setCurrentProjectId: (currentProjectId: number) => set({currentProjectId}),
    setKanbanBoardId: (kanbanBoardId: number) => set({kanbanBoardId}),
    setIssuedId: (issueId: number | null) => set({issueId}),
    setClient: (client: CompatClient | null) => {
        set({client})
        get().client!.connect(
            {},
            () => {
                get().client && get().currentProjectId && get().kanbanBoardId &&
                get().client!.subscribe(`/sub/projects/${get().currentProjectId}/kanbanRoom/${get().kanbanBoardId}`,
                    (message) => {
                        const issues = JSON.parse(message.body); // issues 배열이 IssueProp 타입을 갖는다고 명시합니다.
                        const {selectedLabel, selectedMember, selectedImpotance} = get();

                        const filteredIssues = issues.issues.filter((issue: IssueProp) => { // 여기서 issue의 타입을 명시적으로 선언합니다.
                            return (!selectedLabel || issue.label.labelId === selectedLabel) &&
                                (!selectedMember || issue.charger.id === selectedMember) &&
                                (!selectedImpotance || issue.importance === selectedImpotance);
                        });

                        get().setIssues(filteredIssues);
                    }
                );

                get().client!.subscribe(`/sub/projects/${get().currentProjectId}/kanbanRoom/${get().kanbanBoardId}/label`,
                    (message) => {
                        const data = JSON.parse(message.body);
                        get().setLabels(data.labels);
                    }
                );
            }
        )
    },
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
        await client.get(`/api/v1/projects/${projectId}/kanbanboards/${moduleId}`)
            .then((reponse) => {
                const kanbanBoard: KanbanBoardProp = reponse.data;
                get().setIssues(kanbanBoard.issues);
            });

    },
    getIssues: async (projectId: number, moduleId: number, chargerId: number | null, labelId: number | null, importance: number | null) => {
        await client.get(`/api/v1/projects/${projectId}/kanbanboards/${moduleId}/issues`,
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
        await client.get(`/api/v1/projects/${projectId}/kanbanboards/${moduleId}/label`)
            .then((response) => {
                const labels = response.data;
                get().setLabels(labels.labels);
            });
    },

    createIssue: async (projectId: number, moduleId: number, writeIssueRequest: CreateIssueProp) => {
        await client.post(`/api/v1/projects/${projectId}/kanbanboards/${moduleId}/issues`,
            writeIssueRequest
        ).then(() => {
            get().sendIssue();
        });
    },

    getIssueDetail: async (projectId: number, moduleId: number, issueId: number): Promise<IssueDetailProp> => {
        try {
            const response = await client.get(`/api/v1/projects/${projectId}/kanbanboards/${moduleId}/issues/${issueId}`);
            return response.data;
        } catch (error) {
            console.error("Failed to fetch issue details: ", error);
            throw error;
        }
    },

    updateIssueLabel: async (projectId: number, moduleId: number, issueId: number, labelId: number) => {
        await client.patch(`/api/v1/projects/${projectId}/kanbanboards/${moduleId}/issues/${issueId}/label`,
            {
                labelId: labelId
            }
        ).then(() => {
            get().sendIssue();
        })
    },

    updateIssueStatus: async (projectId: number, moduleId: number, issueId: number, status: number) => {
        await client.patch(`/api/v1/projects/${projectId}/kanbanboards/${moduleId}/issues/${issueId}/status`,
            {
                status: status
            }
        ).then(() => {
            get().sendIssue();
        })
    },

    updateIssueImportance: async (projectId: number, moduleId: number, issueId: number, importance: number) => {
        await client.patch(`/api/v1/projects/${projectId}/kanbanboards/${moduleId}/issues/${issueId}/importance`,
            {
                importance: importance
            }
        ).then(() => {
            get().sendIssue();
        })
    },

    updateIssueMember: async (projectId: number, moduleId: number, issueId: number, chargerId: number) => {
        await client.patch(`/api/v1/projects/${projectId}/kanbanboards/${moduleId}/issues/${issueId}/member`,
            {
                chargerId: chargerId
            }
        ).then(() => {
            get().sendIssue();
        })
    },

    updateIssue: async (projectId: number, moduleId: number, issueId: number, updateIssueRequest: UpdateIssueRequest) => {
        await client.patch(`/api/v1/projects/${projectId}/kanbanboards/${moduleId}/issues/${issueId}`,
            updateIssueRequest
        ).then(() => {
            get().sendIssue();
        })
    },

    deleteIssue: async (projectId: number, moduleId: number, issueId: number) => {
        await client.delete(`/api/v1/projects/${projectId}/kanbanboards/${moduleId}/issues/${issueId}`)
            .then(() => {
                get().sendIssue();
            })
    },
    getMembersInproject: async (projectId: number) => {
        await client.get(`/api/v1/projects/${projectId}`)
            .then((response) => {
                const projectMember = response.data;
                get().setprojectMembers(projectMember);
            }).catch((error) => {
                console.log(error);
            })
    },
    createLabel: async (projectId: number, moduleId: number, name: string) => {
        await client.post(`/api/v1/projects/${projectId}/kanbanboards/${moduleId}/label`,
            {
                name: name
            }).then(() => {
            // 라벨 목록 갱신
            get().sendlabel()
        })
    },
    deleteLabel: async (projectId: number, moduleId: number, labelId: number) => {
        await client.delete(`/api/v1/projects/${projectId}/kanbanboards/${moduleId}/label/${labelId}`)
            .then(() => {
                // 라벨 목록 갱신
                get().sendlabel()
            })
    },
    sendIssue: () => {
        if (get().client && get().client?.connected) {
            // STOMP 메시지 전송
            get().client?.send(`/pub/projects/${get().currentProjectId}/kanbanboards/${get().kanbanBoardId}`,
                {},
            );
        } else {
            console.log('Not connected to WebSocket');
        }
    },
    sendlabel: () => {
        if (get().client && get().client?.connected) {
            // STOMP 메시지 전송
            get().client?.send(`/pub/projects/${get().currentProjectId}/kanbanboards/${get().kanbanBoardId}/label`,
                {},
            );
        } else {
            console.log('Not connected to WebSocket');
        }
    }
}));