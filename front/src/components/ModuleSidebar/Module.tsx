import { Divider, List, ListItem, Typography } from "@mui/material";
import Box from "@mui/material/Box";
import styled from "@mui/system/styled";
import React from "react";
import Circle from "@mui/icons-material/Circle";
import { ModuleResponse } from "../../services/Module/ModuleApi";
import { useModuleStore } from "../../stores/ModuleStorage";

type moduleProps = {
    moduleCategory: React.ReactNode;
    moduleItems: Array<ModuleResponse>;
};

const ModuleCategoryBox = styled(Box)({
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    marginLeft: 8,
    marginRight: 8,
});

const ModuleItemTypography = styled(Typography)({
    maxWidth: "100%",
    overflow: "hidden",
    whiteSpace: "nowrap",
    textOverflow: "ellipsis",
    color: "#49454F",
    fontSize: 12,
});

const ModuleItemsTypeGuard = (moduleItems: Array<ModuleResponse>): React.ReactNode => {
    const { currentModuleContent, setCurrentModuleContent } = useModuleStore();

    if (moduleItems === null || moduleItems === undefined || moduleItems.length == 0) {
        return null;
    } else {
        return (
            <Box>
                <List>
                    {moduleItems.map((item, index) => (
                        <ListItem
                            key={index}
                            onClick={() => {
                                setCurrentModuleContent(item.id, item.category);
                                console.log(currentModuleContent);
                            }}
                            sx={{
                                cursor: "pointer",
                            }}
                        >
                            <Circle sx={{ fontSize: 8, marginRight: 1.5, color: "#49454F" }} />
                            <ModuleItemTypography variant="button">{item.title}</ModuleItemTypography>
                        </ListItem>
                    ))}
                </List>
                <Divider
                    variant="middle"
                    sx={{
                        marginBottom: 2,
                    }}
                />
            </Box>
        );
    }
};

const Module = ({ moduleCategory, moduleItems }: moduleProps) => {
    return (
        <Box
            sx={{
                marginLeft: "4px",
                whiteSpace: "nowrap",
            }}
        >
            <ModuleCategoryBox>
                <Typography fontSize={16} fontWeight={"500"} color={"#49454F"}>
                    {moduleCategory}
                </Typography>
            </ModuleCategoryBox>
            {ModuleItemsTypeGuard(moduleItems)}
        </Box>
    );
};

export default Module;
