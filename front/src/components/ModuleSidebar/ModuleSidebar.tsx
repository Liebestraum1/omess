import Box from "@mui/material/Box";
import styled from "@mui/system/styled";
import ProjectName from "./ProjectName.tsx";
import Module from "./Module.tsx";
import { useProjectStore } from "../../stores/ProjectStorage.tsx";
import { useEffect, useState } from "react";
import { ModuleResponse, getModulesApi } from "../../services/Module/ModuleApi.ts";
import { Typography } from "@mui/material";

type GroupModules = {
    [key: string]: ModuleResponse[];
};

const ModuleSidebarBox = styled(Box)({
    width: 200,
    backgroundColor: "#E8DEF8",
    overflow: "auto",

    "&::-webkit-scrollbar": {
        width: "8px", // 스크롤바의 너비
    },
    "&::-webkit-scrollbar-track": {
        background: "transparent",
    },
    "&::-webkit-scrollbar-thumb": {
        display: "none",
        backgroundColor: "purple", // 스크롤바 썸(움직이는 부분)의 배경색
        borderRadius: "4px", // 스크롤바 썸의 모서리 둥글기
    },
});

const ModuleSidebar = () => {
    const { selectedProjectName, selectedProjectId } = useProjectStore();
    const [groupedModules, setGroupedModules] = useState<GroupModules>({});

    useEffect(() => {
        if (selectedProjectId != undefined) {
            getModulesApi(selectedProjectId).then((data: any) => {
                const modules = data.reduce((acc: any, module: ModuleResponse) => {
                    const key = module.category;
                    if (!acc[key]) {
                        acc[key] = [];
                    }

                    acc[key].push(module);
                    return acc;
                }, {});
                setGroupedModules(modules);
            });
        }
    }, [selectedProjectId]);

    return (
        <ModuleSidebarBox>
            <ProjectName projectName={selectedProjectName} />

            {Object.keys(groupedModules).length === 0 ? (
                <Typography> 모듈 생성 </Typography> // TODO => 모듈 생성 버튼
            ) : (
                Object.keys(groupedModules).map((key) => (
                    <Module
                        moduleCategory={key}
                        key={key}
                        moduleItems={groupedModules[key].map((module: ModuleResponse) => module.title)}
                    ></Module>
                ))
            )}
        </ModuleSidebarBox>
    );
};

export default ModuleSidebar;
