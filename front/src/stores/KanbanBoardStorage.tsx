import {create} from "zustand";
import {LabelProp} from "../types/Label/Label.ts";
import {MemberProp} from "../types/Member/Member.ts";

type KanbanBoardStorage = {
    labels: LabelProp[];
    projectMembers: MemberProp[];
    selectedLabel: number | null;
    selectedMember: number | null;
    selectedImpotance: number | null;
    setLabels: (labels: LabelProp[]) => void;
    setprojectMembers: (projectMembers: MemberProp[]) => void;
    setSelectedLabel: (labelId: number | null) => void;
    setSelectedMember: (memberId: number | null) => void;
    setSelectedImpotance: (impotance: number | null) => void;
}

export const useKanbanBoardStore = create<KanbanBoardStorage>((set) => ({
    labels: [],
    projectMembers: [],
    selectedLabel: null,
    selectedMember: null,
    selectedImpotance: null,

    setLabels: (labels: LabelProp[]) => set({labels}),
    setprojectMembers: (projectMembers: MemberProp[]) => set({projectMembers}),
    setSelectedLabel: (labelId: number | null) => set({selectedLabel: labelId}),
    setSelectedMember: (memberId: number | null) => set({selectedMember: memberId}),
    setSelectedImpotance: (impotance: number | null) => set({selectedImpotance: impotance}),
}));