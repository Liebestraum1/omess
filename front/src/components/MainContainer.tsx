import {ReactNode} from "react";
import Box from "@mui/material/Box"
import styled from '@mui/system/styled'

type MainContainerProps = {
    children: ReactNode,
}

const MainContainerComponent = styled(Box)({
    display: 'flex',
    height: '100%'
});

export default function MainContainer({children}: MainContainerProps){
    return (
        <MainContainerComponent>
            {children}
        </MainContainerComponent>
    )
}