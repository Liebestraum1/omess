import { Divider, List, ListItem, Typography } from "@mui/material"
import Box from "@mui/material/Box"
import styled from '@mui/system/styled'
import React from "react"
import AddIcon from '@mui/icons-material/Add';
import Circle from '@mui/icons-material/Circle';
import ArrowRightIcon from '@mui/icons-material/ArrowRight';

type moduleProps = {
    moduleCategory: React.ReactNode;
    moduleItems: Array<React.ReactNode>;
}

const ModuleCategoryBox = styled(Box)({
    display: "flex",
    justifyContent: "space-between",
    marginLeft: 8,
    marginRight: 8,
})

const ModuleItemTypography = styled(Typography)({
    maxWidth: '100%',
    overflow: 'hidden',
    whiteSpace: 'nowrap',
    textOverflow: 'ellipsis',
    color: '#49454F',
    fontSize: 12
})


const ModuleItemsTypeGuard = (moduleItems : Array<React.ReactNode>): React.ReactNode => {
    if(moduleItems === null || moduleItems === undefined || moduleItems.length == 0){
        return null;
    } else {
        return (
            <Box>
                <List>
                    {moduleItems.map((item, index) => (
                        <ListItem key={index}>
                            <Circle sx={{fontSize: 8, marginRight: 1.5, color: "#49454F"}}/>
                            <ModuleItemTypography variant="button">{item}</ModuleItemTypography>
                        </ListItem>)
                    )}
                </List>
                <Divider variant="middle" sx={{
                    marginBottom: 2
                }}/>
            </Box>
        )
    }
}

const Module = ({moduleCategory, moduleItems}: moduleProps) => {
    return (
        <Box >
            <ModuleCategoryBox>
                <Box display={"flex"}>
                    <ArrowRightIcon/>
                    <Typography fontSize={16} fontWeight={"600"} color={"#49454F"}>
                        {moduleCategory}
                    </Typography>
                </Box>
                <AddIcon/>
            </ModuleCategoryBox>
            {ModuleItemsTypeGuard(moduleItems)}
        </Box>
    )
}

export default Module;