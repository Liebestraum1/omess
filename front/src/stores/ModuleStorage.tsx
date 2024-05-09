import { create } from "zustand";

type ModuleContentRequest = {
    moduleId: number;
    category: string;
};

type ModuleStore = {
    currentModuleContent: ModuleContentRequest | undefined;
    setCurrentModuleContent: (moduleId: number, category: string) => void;
    resetModuleContent: () => void;
};

export const useModuleStore = create<ModuleStore>((set) => ({
    currentModuleContent: undefined,

    setCurrentModuleContent: (moduleId: number, category: string) => {
        set(() => ({
            currentModuleContent: {
                moduleId: moduleId,
                category: category,
            },
        }));
    },

    resetModuleContent: () => {
        set(() => ({
            currentModuleContent: undefined,
        }));
    },
}));
