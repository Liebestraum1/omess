import { Box, Typography } from "@mui/material";
import styled from '@mui/system/styled'

type ProjectNameProps = {
    projectName: React.ReactNode
}

const ProjectNameBox = styled(Box)({
    width: '100%',
    height: 48,
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
})

const ProjectNameTypography = styled(Typography)({
    maxWidth: '100%',
    overflow: 'hidden',
    whiteSpace: 'nowrap',
    textOverflow: 'ellipsis',
    fontSize: '16px',
    color: '#49454F',
    fontWeight: 'bold',
    marginInline: '16px',
})


const ProjectName = ({projectName}: ProjectNameProps) => {
    return (
        <ProjectNameBox>
            <ProjectNameTypography>
                {projectName}
            </ProjectNameTypography>
        </ProjectNameBox>
    )
}

export default ProjectName;