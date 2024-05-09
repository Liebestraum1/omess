import { create } from "zustand";
import { Project } from "../types/Project/Project";
import { getProjectApi } from "../services/Project/ProjectApi";
import ProjectName from "../components/ModuleSidebar/ProjecTyphography";

type ProjectStore = {
    projectList: Array<Project> | undefined;
    selectedProjectId: number | undefined;
    selectedProjectName: string | undefined;
    setProjectList: (projectList: Array<Project>) => void;
    setSelectedProjectId: (projcetid: number) => void;
    setSelectedProjectName: (projectName: string) => void;
};

export const useProjectStore = create<ProjectStore>((set) => ({
    projectList: undefined,
    selectedProjectId: undefined,
    selectedProjectName: undefined,
    setProjectList: (projectList: Array<Project>) => {
        set(() => ({
            projectList: projectList,
        }));
    },
    setSelectedProjectId: (projectId: number) => {
        set(() => ({
            selectedProjectId: projectId,
        }));
    },
    setSelectedProjectName: (projectName: string) => {
        set(() => ({
            selectedProjectName: projectName,
        }));
    },
}));
