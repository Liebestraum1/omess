import { Box, Container } from "@mui/material";
import Login from "../components/Login/Login";

const LoginPage = () => {
    return (
        <Container>
            <Box
                sx={{
                    height: "100vh",
                }}
            >
                <Login></Login>
            </Box>
        </Container>
    );
};

export default LoginPage;
