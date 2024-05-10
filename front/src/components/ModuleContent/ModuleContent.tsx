import Box from "@mui/material/Box";
import styled from "@mui/system/styled";
import PathBar from "./PathBar";
import KanbanBoardPage from "../../pages/KanbanBoardPage.tsx";
import ApiSpecificationComponent from "../ApiSpeification/ApiSpecificationComponent.tsx";
import { useModuleStore } from "../../stores/ModuleStorage.tsx";
import { useProjectStore } from "../../stores/ProjectStorage.tsx";

const ModuleContentBox = styled(Box)({
    flex: 7,
    backgroundColor: ".main",
    overflow: "auto",
    "&::-webkit-scrollbar": {
        width: "8px", // 스크롤바의 너비
    },
    "&::-webkit-scrollbar-track": {
        background: "transparent",
    },
    "&::-webkit-scrollbar-thumb": {
        backgroundColor: "purple", // 스크롤바 썸(움직이는 부분)의 배경색
        borderRadius: "4px", // 스크롤바 썸의 모서리 둥글기
    },
});

const ModuleContent = () => {
    const { currentModuleContent } = useModuleStore();
    const { selectedProjectId } = useProjectStore();

    const DisplayModuleContent = () => {
        const currentCategory = currentModuleContent?.category;
        const currentModuleId = currentModuleContent?.moduleId;
        const currentProjectId = selectedProjectId;

        if (currentProjectId == undefined || currentModuleId == undefined) {
            return null;
        }

        switch (currentCategory) {
            case "일정 관리":
                return <KanbanBoardPage projectId={currentProjectId} moduleId={currentModuleId}></KanbanBoardPage>;
            case "API 명세서":
                return (
                    <ApiSpecificationComponent
                        projectId={currentProjectId}
                        apiSpecificationId={currentModuleId}
                    ></ApiSpecificationComponent>
                );
        }
        return null;
    };

    return (
        <ModuleContentBox>
            {/* <PathBar /> */}
            <DisplayModuleContent></DisplayModuleContent>
        </ModuleContentBox>
    );
};

export default ModuleContent;
