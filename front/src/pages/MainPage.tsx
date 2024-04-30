import ProjectSidebar from "../components/ProjectSidebar/ProjectSidebar";
import ModuleSidebar from "../components/ModuleSidebar/ModuleSidebar";
import ModuleContent from "../components/ModuleContent/ModuleContent";
import NavBar from "../components/NavBar/NavBar";
import Box from "@mui/material/Box";
import { useLoginStore } from "../stores/LoginStorage";

const MainPage = () => {
    const { loginStatus } = useLoginStore();
    console.log("현재 로그인 상태: " + loginStatus);

    return (
        <Box sx={{ width: "100%", height: "100vh", display: "flex", flexDirection: "column" }}>
            <Box sx={{ height: "32px" }}>
                <NavBar />
            </Box>
            <Box sx={{ height: `calc(100vh - 32px)`, overflow: "auto", display: "flex" }}>
                <ProjectSidebar />
                <ModuleSidebar />
                <ModuleContent />
            </Box>
        </Box>
    );
};

export default MainPage;
