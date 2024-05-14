import { Divider, Icon, List, ListItem, ListItemButton, SvgIconProps, Typography } from "@mui/material";
import Box from "@mui/material/Box";
import styled from "@mui/system/styled";
import React, { useState } from "react";
import Circle from "@mui/icons-material/Circle";
import ApiIcon from "@mui/icons-material/Api";
import CalendarMonthIcon from "@mui/icons-material/CalendarMonth";
import { ModuleResponse } from "../../services/Module/ModuleApi";
import { useModuleStore } from "../../stores/ModuleStorage";
import { useNavigate } from "react-router-dom";

type moduleProps = {
    moduleCategory: string;
    moduleItems: Array<ModuleResponse>;
};

const ModuleCategoryBox = styled(Box)({
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    marginLeft: "8px",
    marginRight: "8px",
});

const ModuleItemTypography = styled(Typography)({
    maxWidth: "100%",
    overflow: "hidden",
    whiteSpace: "nowrap",
    textOverflow: "ellipsis",
    color: "#49454F",
    fontSize: 14,
});

const iconStyle = {
    fontSize: "16px",
    paddingLeft: "12px",
};
const ModuleCategoryIcon = (moduleCategory: string) => {
    switch (moduleCategory) {
        case "API 명세서":
            return <ApiIcon sx={iconStyle} />;
        case "일정 관리":
            return <CalendarMonthIcon sx={iconStyle} />;
    }
    return null;
};

const ModuleItemsListItems = (moduleCategory: string, moduleItems: Array<ModuleResponse>): React.ReactNode => {
    const { setCurrentModuleContent } = useModuleStore();
    const navigate = useNavigate();

    if (moduleItems === null || moduleItems === undefined || moduleItems.length == 0) {
        return null;
    } else {
        return (
            <Box>
                <List
                    sx={{
                        paddingBottom: "0px",
                    }}
                >
                    <ListItem disablePadding>
                        {ModuleCategoryIcon(moduleCategory)}
                        <Typography fontSize={16} fontWeight={"600"} color={"#49454F"} paddingLeft="8px">
                            {moduleCategory}
                        </Typography>
                    </ListItem>
                    <List
                        sx={{
                            paddingBottom: "0px",
                        }}
                    >
                        {moduleItems.map((item, index) => (
                            <ListItem
                                key={index}
                                disablePadding
                                sx={{
                                    width: "100%",
                                    cursor: "pointer",
                                }}
                            >
                                <ListItemButton
                                    onClick={() => {
                                        setCurrentModuleContent(item.id, item.category);
                                        navigate("/main/module");
                                    }}
                                >
                                    <Circle
                                        sx={{ fontSize: 4, marginLeft: "8px", marginRight: "8px", color: "#49454F" }}
                                    />
                                    <ModuleItemTypography variant="button">{item.title}</ModuleItemTypography>
                                </ListItemButton>
                            </ListItem>
                        ))}
                    </List>
                </List>
            </Box>
        );
    }
};

const Module = ({ moduleCategory, moduleItems }: moduleProps) => {
    return (
        <Box
            sx={{
                whiteSpace: "nowrap",
            }}
        >
            <ModuleCategoryBox></ModuleCategoryBox>
            {ModuleItemsListItems(moduleCategory, moduleItems)}
        </Box>
    );
};

export default Module;
