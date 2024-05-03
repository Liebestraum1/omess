import ProjectSidebar from "../components/ProjectSidebar/ProjectSidebar";
import ModuleSidebar from "../components/ModuleSidebar/ModuleSidebar";
import ModuleContent from "../components/ModuleContent/ModuleContent";
import NavBar from "../components/NavBar/NavBar";
import Box from "@mui/material/Box";
import { useEffect } from "react";
import { useSignInStore } from "../stores/SignInStorage";
import { useNavigate } from "react-router-dom";

const MainPage = () => {
    const { memberNickname } = useSignInStore();
    const navigate = useNavigate();

    useEffect(() => {
        if (memberNickname === undefined) {
            navigate("/");
        }
    }, [memberNickname]);

    return (
        <Box sx={{ width: "100%", height: "100vh", display: "flex", flexDirection: "column" }}>
            <Box sx={{ height: "40px" }}>
                <NavBar />
            </Box>
            <Box sx={{ height: `calc(100vh - 40px)`, overflow: "auto", display: "flex" }}>
                <ProjectSidebar />
                <ModuleSidebar />
                <ModuleContent />
            </Box>
        </Box>
    );
};

export default MainPage;
