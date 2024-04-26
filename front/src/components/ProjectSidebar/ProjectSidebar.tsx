import { Box, styled } from "@mui/material";
import ProjectFab from "./ProjectFab";
import AddIcon from "@mui/icons-material/Add";

const ProjectSidebarBox = styled(Box)({
    display: "flex",
    alignItems: "flex-start",
    flexDirection: "column",
    width: 64,
    minWidth: 64,
    backgroundColor: "#4F378B",
    overflow: "auto",
    "&::-webkit-scrollbar": {
        width: "0px", // 스크롤바의 너비
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

const ProjectSidebar = () => {
    /**
     * 현재 유저가 속한 프로젝트 정보 조회 API 호출
     * 해당 API 결과값을 List로 받기
     * 각 Fab은 개별 프로젝트를 조회하는 API를 호출하고, 그 결과값을 ModuleSidebar로 보냄
     */

    const fabComponent: React.ReactNode = Array.from({ length: 5 }, (_, index) => <ProjectFab content={String(index)} key={index} />);

    return (
        // 더하기 버튼에 대해서는 새로운 프로젝트를 생성하는 API를 호출
        <ProjectSidebarBox>
            {fabComponent}
            <ProjectFab content={<AddIcon />} />
        </ProjectSidebarBox>
    );
};

export default ProjectSidebar;
