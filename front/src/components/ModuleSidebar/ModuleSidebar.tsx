import Box from "@mui/material/Box";
import styled from "@mui/system/styled";
import ProjectName from "./ProjectName";
import Module from "./Module";
import ChatListComponent from "../chat/ChatListComponent.tsx";

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

/**
 * ProjectSidebar의 Fab이 보낸 정보를 받아와서 파라미터로 담음
 * 프로젝트 이름, 프로젝트에 포함된 모듈의 리스트
 * 모듈은 모듈 카테고리(어떤 종류의 모듈인지)와 해당 카테고리에 있는 모듈의 리스트(아이템)를 받음
 */
const ModuleSidebar = () => {
    return (
        <ModuleSidebarBox>
            <ProjectName projectName={"A301 자율 프로젝트가너무길어지면어떻게될까나"} />
            <Box display="flex" flexDirection="column">
                <ChatListComponent projectId={1} />
            </Box>
            <Module moduleCategory={"채팅"} moduleItems={["전승열이또너무길어지면어떻게될까나", "휘파람"]}></Module>
            <Module moduleCategory={"일정 관리"} moduleItems={["전승열이또너무길어지면어떻게될까나", "휘파람"]}></Module>
            <Module moduleCategory={"API 명세서"} moduleItems={["전승열이또너무길어지면어떻게될까나", "휘파람"]}></Module>
            <Module moduleCategory={"채팅"} moduleItems={["전승열이또너무길어지면어떻게될까나", "휘파람"]}></Module>
            <Module moduleCategory={"채팅"} moduleItems={["전승열이또너무길어지면어떻게될까나", "휘파람"]}></Module>
            <Module moduleCategory={"채팅"} moduleItems={["전승열이또너무길어지면어떻게될까나", "휘파람"]}></Module>
        </ModuleSidebarBox>
    );
};

export default ModuleSidebar;
