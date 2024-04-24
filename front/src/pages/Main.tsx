import Box from "@mui/material/Box"
import ProjectSidebar from "../components/ProjectSidebar"
import ModuleSidebar from "../components/ModuleSidebar"
import ContentBox from "../components/ContentBox"
import NavBar from "../components/NavBar"

export default function MainPage(){
    return(
        <Box sx={{ width: '100%', height: '100vh', display: 'flex', flexDirection: 'column'}}>
            <Box sx={{height: '32px'}}>
                <NavBar/>
            </Box>
            <Box sx={{ height: `calc(100vh - 32px)`, overflow: 'auto', display: 'flex'}}>
                    <ProjectSidebar/>
                    <ModuleSidebar/>
                    <ContentBox/>
            </Box>
        </Box>
    )
}