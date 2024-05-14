import { useEffect, useState } from "react";

import { Alert, AlertTitle, Box, Button, Snackbar, styled } from "@mui/material";
import AddIcon from "@mui/icons-material/Add";

import { useProjectStore } from "../../stores/ProjectStorage";
import { useModuleStore } from "../../stores/ModuleStorage";

import { Project } from "../../types/Project/Project";
import { AlertContent } from "../../types/common/Alert";
import { getProjectApi } from "../../services/Project/ProjectApi";
import ProjectFab from "./ProjectFab";
import ProjectInvitationModal from "./ProjectInvitationModal";

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

const alertButton = (action: () => void) => {
    return <Button onClick={action}>확인</Button>;
};

const ProjectSidebar = () => {
    //  * 해당 API 결과값을 List로 받기
    //  * 각 Fab은 개별 프로젝트를 조회하는 API를 호출하고, 그 결과값을 ModuleSidebar로 보냄
    const { projectList, setProjectList, setSelectedProjectId, setSelectedProjectName } = useProjectStore();
    const { resetModuleContent } = useModuleStore();

    const [showAlert, setShowAlert] = useState<boolean>(false);
    const [modalOpen, setModalOpen] = useState(false);
    const [alertContent, setAlertContent] = useState<AlertContent>({
        severity: undefined,
        title: "",
        content: "",
    });

    // alertContent 변경시마다 호출
    useEffect(() => {
        getProjectApi()
            .then((data: any) => {
                const dataList: Array<Project> = data["projects"];
                setProjectList(dataList);
            })
            .catch((error) => {});
    }, [alertContent]);

    return (
        <ProjectSidebarBox>
            {/* 프로젝트 목록 */}
            {projectList?.map((project) => (
                <ProjectFab
                    projectFabContent={project.name}
                    onClick={() => {
                        setSelectedProjectId(project.projectId);
                        setSelectedProjectName(project.name);
                        resetModuleContent();
                    }}
                    key={project.projectId}
                />
            ))}

            {/* 프로젝트 추가 아이콘 */}
            <ProjectFab onClick={() => setModalOpen(true)} projectFabContent={<AddIcon />} />

            {/* 프로젝트 생성 모달 */}
            <ProjectInvitationModal
                open={modalOpen}
                onClose={() => setModalOpen(false)}
                showAlert={() => setShowAlert(true)}
                setAlertContent={(alertContent: AlertContent) => setAlertContent(alertContent)}
            ></ProjectInvitationModal>

            {/* 프로젝트 생성 성공 알람 */}
            <Snackbar
                open={showAlert}
                anchorOrigin={{ horizontal: "center", vertical: "bottom" }}
                autoHideDuration={3000}
                onClose={() => setShowAlert(false)}
            >
                <Alert severity={alertContent.severity} action={alertButton(() => setShowAlert(false))}>
                    <AlertTitle>{alertContent.title}</AlertTitle>
                    {alertContent.content}
                </Alert>
            </Snackbar>
        </ProjectSidebarBox>
    );
};

export default ProjectSidebar;
