import Box from "@mui/material/Box";
import Fab from "@mui/material/Fab";
import Typography from "@mui/material/Typography";
import React from "react";

type ProjectFabPropsFromProjectSidebar = {
    fabContent: React.ReactNode;
    onClick: () => void;
};

const fabContentTypeGuard = (fabContent: React.ReactNode): React.ReactNode => {
    if (typeof fabContent === "string") {
        // String인 겨우
        return (
            <Typography
                sx={{
                    color: "#6750A4",
                    fontSize: 14,
                    fontWeight: "Bold",
                }}
            >
                {fabContent}
            </Typography>
        );
    } else {
        // String이 아닌 경우 (아이콘 / 사진 등)
        return (
            <Box color={"#6750A4"} display={"flex"}>
                {fabContent}
            </Box>
        );
    }
};

const ProjectFab = ({ fabContent, onClick }: ProjectFabPropsFromProjectSidebar) => {
    return (
        <Box
            sx={{
                height: 64,
                width: 64,
                minHeight: 64,
                maxHeight: 64,
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
            }}
        >
            <Fab
                aria-label="add"
                onClick={onClick}
                sx={{
                    height: 48,
                    width: 48,
                    borderRadius: 4,
                    color: "#ECE6F0",
                }}
            >
                {fabContentTypeGuard(fabContent)}
            </Fab>
        </Box>
    );
};

export default ProjectFab;
