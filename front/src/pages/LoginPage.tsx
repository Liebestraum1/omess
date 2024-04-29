import { Box, styled } from "@mui/material";
import Login from "../components/Login/Login";

const LoginBox = styled(Box)({
    height: "100vh",
});

const LoginPage = () => {
    return (
        <LoginBox>
            <Login></Login>
        </LoginBox>
    );
};

export default LoginPage;
