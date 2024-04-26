import Box from "@mui/material/Box";
import Fab from "@mui/material/Fab";
import Typography from "@mui/material/Typography";
import React from "react";

type ProjectFabProps = {
    content: React.ReactNode;
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

const ProjectFab = ({ content }: ProjectFabProps) => {
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
                sx={{
                    height: 48,
                    width: 48,
                    borderRadius: 4,
                    color: "#ECE6F0",
                }}
            >
                {fabContentTypeGuard(content)}
            </Fab>
        </Box>
    );
};

export default ProjectFab;
