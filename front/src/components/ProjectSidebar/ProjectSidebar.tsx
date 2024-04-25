import Box from "@mui/material/Box"
import styled from '@mui/system/styled'
import ProjectFab from "./ProjectFab"
import AddIcon from '@mui/icons-material/Add';

const ProjectSidebarBox = styled(Box)({
    display: "flex",
    alignItems: "center",
    flexDirection: "column",
    width: 64,
    backgroundColor: "#4F378B",
});

const ProjectSidebar = () => {
    /**
     * 현재 유저가 속한 프로젝트 정보 조회 API 호출
     * 해당 API 결과값을 List로 받기
     * 각 Fab은 개별 프로젝트를 조회하는 API를 호출하고, 그 결과값을 ModuleSidebar로 보냄
     */

    const fabComponent: React.ReactNode = Array.from({length : 5}, (_, index) => (
        <ProjectFab content={String(index)} key={index}/>
    ));

    return (
        // 더하기 버튼에 대해서는 새로운 프로젝트를 생성하는 API를 호출
        <ProjectSidebarBox>
            {fabComponent}
            <ProjectFab content={<AddIcon/>}/>
        </ProjectSidebarBox>
    )
};

export default ProjectSidebar;