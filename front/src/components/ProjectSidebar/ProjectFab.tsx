import { keyframes, styled } from "@mui/material";
import Box from "@mui/material/Box";
import Fab from "@mui/material/Fab";
import Typography from "@mui/material/Typography";
import React from "react";

const ProjectNameAnimation = keyframes` // 프로젝트 이름이 Fab을 초과할 경우 보여줄 애니메이션
  0% {
    transform: translateX(0%);
  }
  100% {
    transform: translateX(-100%);
  }`;

type ProjectFabPropsFromProjectSidebar = {
    projectFabContent: React.ReactNode;
    onClick: () => void;
};

const ProjectFabBox = styled(Box)({
    height: 64,
    width: 64,
    minHeight: 64,
    maxHeight: 64,
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
});

const ProjectNameTyphography = styled(Typography)({
    color: "#6750A4",
    fontSize: 14,
    fontWeight: "bold",
    whiteSpace: "nowrap",
    display: "flex",
});

const AnimateProjectNameTyphograpy = styled(ProjectNameTyphography)(() => ({
    "&:hover": {
        animation: `${ProjectNameAnimation} 10s linear infinite`,
        textOverflow: "clip",
    },
}));

const ProjectFabContent = (projectFabContent: React.ReactNode) => {
    if (typeof projectFabContent === "string") {
        if (projectFabContent.length < 5) {
            return <ProjectNameTyphography>{projectFabContent}</ProjectNameTyphography>;
        } else {
            return <AnimateProjectNameTyphograpy>{projectFabContent}</AnimateProjectNameTyphograpy>;
        }
    } else {
        return <ProjectNameTyphography>{projectFabContent}</ProjectNameTyphography>;
    }
};

const ProjectFab = ({ projectFabContent, onClick }: ProjectFabPropsFromProjectSidebar) => {
    return (
        <ProjectFabBox>
            <Fab
                aria-label="add"
                onClick={onClick}
                sx={{
                    height: 48,
                    width: 48,
                    borderRadius: 4,
                    color: "#ECE6F0",
                    overflow: "hidden",
                }}
            >
                {ProjectFabContent(projectFabContent)}
            </Fab>
        </ProjectFabBox>
    );
};

export default ProjectFab;
