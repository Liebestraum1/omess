import Box from "@mui/material/Box";
import styled from "@mui/system/styled";
import PathBar from "./PathBar";
import KanbanBoardPage from "../../pages/KanbanBoardPage.tsx";
import ApiSpecificationComponent from "../ApiSpeification/ApiSpecificationComponent.tsx";

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
    return (
        <ModuleContentBox>
            <PathBar />
            <ApiSpecificationComponent projectId={1} apiSpecificationId={1} />
        </ModuleContentBox>
    );
};

export default ModuleContent;
