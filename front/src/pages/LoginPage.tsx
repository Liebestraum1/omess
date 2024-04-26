import { Box, Container, styled } from "@mui/material";
import Login from "../components/Login/Login";

const LoginBox = styled(Box)({
    height: "100vh",
});

const LoginPage = () => {
    return (
        <Container>
            <LoginBox>
                <Login></Login>
            </LoginBox>
        </Container>
    );
};

export default LoginPage;
