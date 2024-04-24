import Box from "@mui/material/Box"
import styled from '@mui/system/styled'

const ContentBoxComponent = styled(Box)({
    flex: 7,
    backgroundColor: '.main',
})

export default function ContentBox(){
    return (
        <ContentBoxComponent>
        </ContentBoxComponent>
    )
}